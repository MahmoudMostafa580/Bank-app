package com.mahmoud.bankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mahmoud.bankapp.R
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.databinding.FragmentCustomersBinding


class CustomersFragment : Fragment() {

    val args: CustomersFragmentArgs by navArgs()
    lateinit var customersAdapter: CustomersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCustomersBinding =
            FragmentCustomersBinding.inflate(inflater, container, false)

        val customerId = args.customerId
        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao
        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        val customersViewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)

        customersViewModel.getAllCustomersExceptOne(customerId)
            .observe(viewLifecycleOwner) { value ->
                customersAdapter = CustomersAdapter(value)
                binding.customersList.adapter = customersAdapter
                customersAdapter.setOnItemClickListener(object :
                    CustomersAdapter.OnItemClickListener {
                    override fun onItemClick(itemView: View, position: Int) {
                        //move to transfer process screen
                        val userFromId = customerId
                        val userToId = value[position].userId
                        val action = CustomersFragmentDirections.actionCustomersFragmentToTransferOperFragment(userFromId, userToId)
                        itemView.findNavController().navigate(action)
                    }
                })
            }

        return binding.root
    }


}