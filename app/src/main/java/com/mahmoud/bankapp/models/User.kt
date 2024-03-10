package com.mahmoud.bankapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var currentBalance: Double
)