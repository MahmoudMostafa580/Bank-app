package com.mahmoud.bankapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mahmoud.bankapp.database.User

@Dao
interface CustomersDao {
    @Insert
    fun insertCustomer(users: User)

    @Update
    fun updateBalance(user: User)

    @Query("SELECT * FROM users_table ORDER BY userId ASC")
    fun getAllCustomers(): List<User>

    @Query("SELECT * FROM users_table WHERE userId = :id")
    fun getCustomer(id: Long) : User?

    @Query("SELECT * FROM users_table WHERE userId != :userId")
    fun getAllCustomersExceptOne(userId: Long) : List<User>


}