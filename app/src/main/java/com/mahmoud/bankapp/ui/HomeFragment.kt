package com.mahmoud.bankapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mahmoud.bankapp.R
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var customersAdapter: CustomersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao

        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        val customersViewModel = ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)

        customersViewModel.allCustomers.observe(viewLifecycleOwner) { value ->
            customersAdapter = CustomersAdapter(value)
            binding.customersList.adapter = customersAdapter
            customersAdapter.setOnItemClickListener(object: CustomersAdapter.OnItemClickListener{
                override fun onItemClick(itemView: View, position: Int) {
                    val action = HomeFragmentDirections.actionHomeFragmentToCustomerDetailsFragment(value[position].userId)
                    Navigation.findNavController(itemView).navigate(action)
                }
            })
        }



        return binding.root
    }

}