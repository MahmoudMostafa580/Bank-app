package com.mahmoud.bankapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mahmoud.bankapp.database.TransfersDao
import com.mahmoud.bankapp.models.Transfer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransfersViewModel(
    val transfersDao: TransfersDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertTransfer(transfer: Transfer) {
        uiScope.launch {
            insert(transfer)
        }
    }

    private suspend fun insert(transfer: Transfer) {
        withContext(Dispatchers.IO){
            transfersDao.insertTransfer(transfer)
        }
    }

}