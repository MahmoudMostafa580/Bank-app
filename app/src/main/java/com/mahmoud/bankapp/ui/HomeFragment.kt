package com.mahmoud.bankapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mahmoud.bankapp.data.CustomerViewModelFactory
import com.mahmoud.bankapp.data.CustomersViewModel
import com.mahmoud.bankapp.database.BankDatabase
import com.mahmoud.bankapp.databinding.FragmentHomeBinding
import com.mahmoud.bankapp.models.User

class HomeFragment : Fragment() {
    private lateinit var customersAdapter: CustomersAdapter
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = BankDatabase.getInstance(application).customerDao

        val viewModelFactory = CustomerViewModelFactory(dataSource, application)
        val customersViewModel = ViewModelProvider(this, viewModelFactory).get(CustomersViewModel::class.java)


        //Loading customers into recyclerView
        customersViewModel.allCustomers.observe(viewLifecycleOwner) { value ->
            if (value.isNotEmpty() || value!=null){
                val customers: List<User> = value
                customersAdapter = CustomersAdapter(customers)
                binding.customersList.adapter = customersAdapter
                binding.customersList.setHasFixedSize(true)
                customersAdapter.notifyDataSetChanged()
                //Handle item click in recycler
                customersAdapter.setOnItemClickListener(object: CustomersAdapter.OnItemClickListener{
                    override fun onItemClick(itemView: View, position: Int) {
                        val action = HomeFragmentDirections.actionHomeFragmentToCustomerDetailsFragment(value[position].userId)
                        Navigation.findNavController(itemView).navigate(action)
                    }
                })

            }else{
                Toast.makeText(requireActivity(), "Error while loading data!", Toast.LENGTH_SHORT).show()
            }

        }



        return binding.root
    }


}