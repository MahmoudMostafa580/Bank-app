package com.mahmoud.bankapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mahmoud.bankapp.database.TransfersDao
import com.mahmoud.bankapp.models.Transfer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransfersViewModel(
    private val transfersDao: TransfersDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    fun insertTransfer(transfer: Transfer) {
        Log.v("Transfer Adding..: ", "Transfer beginning...")
        uiScope.launch {
            addTransfer(transfer)

        }


    }

    private suspend fun addTransfer(transfer: Transfer) {
        withContext(Dispatchers.IO) {
            Log.v("Transfer Adding..: ", "Transfer in progress..")
            transfersDao.insertToTable(transfer)
            Log.v("Transfer Adding..: ", "Transfer added successfully..")
        }
        _navigateToHome.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}