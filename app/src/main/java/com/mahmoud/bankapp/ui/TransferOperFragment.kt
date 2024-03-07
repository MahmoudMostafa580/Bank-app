package com.mahmoud.bankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.databinding.FragmentTransferOperBinding


class TransferOperFragment : Fragment() {

    val args: TransferOperFragmentArgs by navArgs()
    lateinit var customersViewModel: CustomersViewModel

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
        var userFromId = args.userFromId
        var userToId = args.userToId

        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao
        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        customersViewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)


        binding.confirmTransferBtn.setOnClickListener {
            val amount = 1000.0 // test value
            var sendResult = false
            var receiveResult = false
            customersViewModel.getSpecificCustomer(userFromId)
                .observe(viewLifecycleOwner, Observer { value ->
                    var userCurrentBalance = value.currentBalance
                    if (amount <= userCurrentBalance) {
                        userCurrentBalance = userCurrentBalance.minus(amount)
                        sendResult = updateSenderBalance(userFromId, userCurrentBalance)
                    }
                })
            customersViewModel.getSpecificCustomer(userToId)
                .observe(viewLifecycleOwner, Observer { value ->
                    var userCurrentBalance = value.currentBalance
                    userCurrentBalance = userCurrentBalance.plus(amount)
                    receiveResult = updateReceiverBalance(userToId, userCurrentBalance)
                })
            if (sendResult && receiveResult) {
                Toast.makeText(requireActivity(), "Transfer successfully", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        return binding.root
    }

    private fun updateReceiverBalance(userToId: Long, balance: Double): Boolean {
        var result1 = false
        customersViewModel.updateNewBalance(userToId, balance)
            .observe(viewLifecycleOwner, Observer { value ->
                result1 = value
            })
        return result1
    }

    private fun updateSenderBalance(userFromId: Long, balance: Double): Boolean {
        var result2 = false
        customersViewModel.updateNewBalance(userFromId, balance)
            .observe(viewLifecycleOwner, Observer { value ->
                result2 = value
            })
        return result2
    }

}