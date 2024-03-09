package com.mahmoud.bankapp.data

import android.app.Application
import android.util.Log
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
    private var senderCustomer = MutableLiveData<User>()
    private var receiverCustomer = MutableLiveData<User>()
    private var allCustomersExceptOne = MutableLiveData<List<User>>()
    var isSuccess = MutableLiveData<Int>()
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

    fun getSpecificCustomer(customerId: Long): MutableLiveData<User> {
        uiScope.launch {
            senderCustomer.value = getCustomer(customerId)
        }
        return senderCustomer
    }

    fun getSenderCustomer(customerId: Long): MutableLiveData<User> {
        uiScope.launch {
            senderCustomer.value = getCustomer(customerId)
        }
        return senderCustomer
    }

    fun getReceiverCustomer(customerId: Long): MutableLiveData<User> {
        uiScope.launch {
            receiverCustomer.value = getCustomer(customerId)
        }
        return receiverCustomer
    }

    private suspend fun getCustomer(id: Long): User? {
        return withContext(Dispatchers.IO) {
            val customer = customersDao.getCustomer(id)
            customer
        }
    }

    fun getAllCustomersExceptOne(customerId: Long): MutableLiveData<List<User>> {
        uiScope.launch {
            allCustomersExceptOne.value = getCustomersExceptOne(customerId)
        }
        return allCustomersExceptOne
    }

    private suspend fun getCustomersExceptOne(id: Long): List<User> {
        return withContext(Dispatchers.IO) {
            val customers = customersDao.getAllCustomersExceptOne(id)
            customers
        }
    }

    fun updateNewBalance(userId: Long, balance: Double) {
        uiScope.launch {
            isSuccess.value = updateBalance(userId, balance)
            Log.v("is successful", isSuccess.value.toString())
        }
    }

    private suspend fun updateBalance(userId: Long, balance: Double): Int {
        return withContext(Dispatchers.IO) {
            customersDao.updateBalance(userId, balance)
        }
    }


}