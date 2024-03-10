package com.mahmoud.bankapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mahmoud.bankapp.models.User

@Dao
interface CustomersDao {
    @Insert
    fun insertCustomer(users: User)

    @Query("UPDATE users_table SET currentBalance = :newBalance WHERE userId= :userId")
    fun updateBalance(userId: Long, newBalance: Double): Int

    @Query("SELECT * FROM users_table ORDER BY userId ASC")
    fun getAllCustomers(): List<User>

    @Query("SELECT * FROM users_table WHERE userId = :id")
    fun getCustomer(id: Long) : User?

    @Query("SELECT * FROM users_table WHERE userId != :userId")
    fun getAllCustomersExceptOne(userId: Long) : List<User>


}