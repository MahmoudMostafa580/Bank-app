package com.mahmoud.bankapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mahmoud.bankapp.models.Transfer

@Dao
interface TransfersDao {

    @Insert
    fun insertTransfer(transfer: Transfer)

    @Query("SELECT * FROM transfers_table ORDER BY transferDate DESC")
    fun getAllTransfers(): LiveData<List<Transfer>>
}