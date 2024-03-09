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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.database.User
import com.mahmoud.bankapp.databinding.FragmentTransferOperBinding


class TransferOperFragment : Fragment() {

    private val args: TransferOperFragmentArgs by navArgs()
    private var sendResult: Int = 0
    private var receiveResult: Int = 0
    lateinit var customersViewModel: CustomersViewModel

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
        val binding: FragmentTransferOperBinding =
            FragmentTransferOperBinding.inflate(inflater, container, false)
        val userFromId = args.userFromId
        val userToId = args.userToId

        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao
        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        customersViewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)


        customersViewModel.getSenderCustomer(userFromId)
            .observe(viewLifecycleOwner, Observer { value ->
                sender = value
                val senderCurrentBalance = value.currentBalance
                val senderName = value.firstName + value.lastName
                Log.v("Sender name :", senderName)
                binding.senderNameTxt.text = senderName
                binding.availableAmountValue.text = senderCurrentBalance.toString()

            })

        customersViewModel.getReceiverCustomer(userToId)
            .observe(viewLifecycleOwner, Observer { value ->
                val receiverName = value.firstName + value.lastName
                Log.v("Receiver name :", receiverName)
                binding.receiverNameTxt.text = receiverName
            })

        binding.confirmTransferBtn.setOnClickListener { view ->
            val amountString = binding.amountLayout.editText?.text.toString()
            val amount = amountString.toDouble()
            if (amount <= sender.currentBalance) {
                val senderNewCurrentBalance = sender.currentBalance.minus(amount)
                updateSenderBalance(userFromId, senderNewCurrentBalance)
            }
            val receiverNewCurrentBalance = receiver.currentBalance.plus(amount)
            updateReceiverBalance(userToId, receiverNewCurrentBalance)

            if (sendResult == 1 && receiveResult == 1) {
                Toast.makeText(requireActivity(), "Transfer successfully", Toast.LENGTH_SHORT)
                    .show()
                //backstack to home fragment..
                val action =
                    TransferOperFragmentDirections.actionTransferOperFragmentToHomeFragment()
                Navigation.findNavController(view).navigate(action)
            } else {
                Toast.makeText(requireActivity(), "Transfer failed!!", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun updateReceiverBalance(userToId: Long, balance: Double) {
        customersViewModel.updateNewBalance(userToId, balance)
        customersViewModel.isSuccess.observe(viewLifecycleOwner, Observer { value ->
            sendResult = value
            Log.v("sendResult", sendResult.toString())
        })
    }

    private fun updateSenderBalance(userFromId: Long, balance: Double) {
        customersViewModel.updateNewBalance(userFromId, balance)
        customersViewModel.isSuccess.observe(viewLifecycleOwner, Observer { value ->
            receiveResult = value
            Log.v("receiveResult", receiveResult.toString())
        })
    }

}