package com.mahmoud.bankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.mahmoud.bankapp.databinding.FragmentCustomerDetailsBinding
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase


class CustomerDetailsFragment : Fragment() {
    val args: CustomerDetailsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCustomerDetailsBinding =
            FragmentCustomerDetailsBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao

        val customerId = args.customerId
        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        val customersViewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)

        customersViewModel.getSpecificCustomer(customerId)
            .observe(viewLifecycleOwner, Observer { value ->
                binding.balanceValueTxt.text = value.currentBalance.toString()
                binding.customerNameValue.text = value.firstName + value.lastName
                binding.customerEmailValue.text = value.email
            })

        binding.transferMoneyCardBtn.setOnClickListener { view ->
            val action = CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToCustomersFragment(customerId)
            Navigation.findNavController(view).navigate(action)
        }
        return binding.root
    }

}