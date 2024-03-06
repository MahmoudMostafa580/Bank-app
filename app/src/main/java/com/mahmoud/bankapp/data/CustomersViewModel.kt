package com.mahmoud.bankapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mahmoud.bankapp.database.CustomersDao
import com.mahmoud.bankapp.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomersViewModel(
    val customersDao: CustomersDao,
    application: Application
) : AndroidViewModel(application) {


    var allCustomers = MutableLiveData<List<User>>()
    var customer = MutableLiveData<User>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getCustomers()
    }

    private fun getCustomers() {
        uiScope.launch {
            allCustomers.value = getAllCustomers()
        }
    }

    private suspend fun getAllCustomers(): List<User> {
        return withContext(Dispatchers.IO) {
            val customers = customersDao.getAllCustomers()
            customers
        }
    }

    fun getSpecificCustomer(customerId: Long):MutableLiveData<User> {
        uiScope.launch {
            customer.value = getCustomer(customerId)
        }
        return customer
    }

    private suspend fun getCustomer(id: Long): User? {
        return withContext(Dispatchers.IO) {
            val customer = customersDao.getCustomer(id)
            customer
        }
    }


}