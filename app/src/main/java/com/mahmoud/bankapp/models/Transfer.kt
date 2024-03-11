package com.mahmoud.bankapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transfers_table")
data class Transfer(
    @ColumnInfo(name = "From")
    var transferFromUserId : Long,

    @ColumnInfo(name = "To")
    var transferToUserId : Long ,

    @ColumnInfo(name = "Amount")
    var moneyAmount : Double,

    @PrimaryKey(autoGenerate = false)
    var transferDate : Long
)