package com.mahmoud.bankapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.data.TransfersViewModel
import com.mahmoud.bankapp.data.TransfersViewModelFactory
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.databinding.FragmentTransferOperBinding
import com.mahmoud.bankapp.models.Transfer
import com.mahmoud.bankapp.models.User

class TransferOperFragment : Fragment() {

    lateinit var binding: FragmentTransferOperBinding
    private val args: TransferOperFragmentArgs by navArgs()
    lateinit var customersViewModel: CustomersViewModel
    lateinit var customersViewModelFactory: CustomerViewModelFactory
    lateinit var transfersViewModel: TransfersViewModel
    lateinit var transfersViewModelFactory: TransfersViewModelFactory

    lateinit var sender: User
    lateinit var receiver: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransferOperBinding.inflate(inflater, container, false)
        val userFromId = args.userFromId
        val userToId = args.userToId

        val application = requireNotNull(this.activity).application
        val dataSourceCustomers = BankDatabase.getInstance(application).customerDao
        val dataSourceTransfers = BankDatabase.getInstance(application).transfersDao

        customersViewModelFactory = CustomerViewModelFactory(dataSourceCustomers, application)
        customersViewModel =
            ViewModelProvider(this, customersViewModelFactory).get(CustomersViewModel::class.java)

        transfersViewModelFactory = TransfersViewModelFactory(dataSourceTransfers, application)
        transfersViewModel =
            ViewModelProvider(this, transfersViewModelFactory).get(TransfersViewModel::class.java)


        customersViewModel.getSenderCustomer(userFromId)
            .observe(viewLifecycleOwner) { value ->
                sender = value
                binding.senderNameTxt.text = value.firstName + value.lastName
                binding.availableAmountValue.text = "${value.currentBalance} EG"

            }

        customersViewModel.getReceiverCustomer(userToId)
            .observe(viewLifecycleOwner) { value ->
                receiver = value
                binding.receiverNameTxt.text = value.firstName + value.lastName
            }

        binding.confirmTransferBtn.setOnClickListener { view -> // all the transfer process
            val amountString = binding.amountLayout.editText?.text.toString()
            if (validateAmount(amountString)) {
                val amount = amountString.toDouble()
                if (amount <= sender.currentBalance) {
                    val senderNewCurrentBalance = sender.currentBalance.minus(amount)
                    customersViewModel.updateNewBalance(userFromId, senderNewCurrentBalance)

                    val receiverNewCurrentBalance = receiver.currentBalance.plus(amount)
                    customersViewModel.updateNewBalance(userToId, receiverNewCurrentBalance)


                    //add the transfer to transfer table
                    val transfer = Transfer(userFromId, userToId, amount, System.currentTimeMillis())
                    transfersViewModel.insertTransfer(transfer)
                    transfersViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
                        if (it == true){
                            this.findNavController().navigate(TransferOperFragmentDirections.actionTransferOperFragmentToHomeFragment())
                            Log.v("Navigating : ", "Start navigating...")
                        }
                    })
                    Toast.makeText(requireActivity(), "Transfer successfully", Toast.LENGTH_SHORT).show()
                } else {
                    binding.amountLayout.error = "Amount not available!"
                }
            }
        }
        return binding.root
    }

    private fun validateAmount(amountString: String): Boolean {
        if (amountString.isEmpty()) {
            binding.amountLayout.error = "Please enter valid amount"
            return false
        }
        return true
    }

}