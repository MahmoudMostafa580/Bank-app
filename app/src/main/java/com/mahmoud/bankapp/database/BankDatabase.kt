package com.mahmoud.bankapp.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mahmoud.bankapp.customersList
import com.mahmoud.bankapp.models.Transfer
import com.mahmoud.bankapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Transfer::class], version = 1, exportSchema = false)
abstract class BankDatabase : RoomDatabase() {
    abstract val customerDao: CustomersDao
    abstract val transfersDao: TransfersDao


    companion object {
        private var INSTANCE: BankDatabase? = null
        fun getInstance(context: Context): BankDatabase {
            synchronized(this) {
                var instance = INSTANCE
                val customersList = customersList()
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BankDatabase::class.java,
                        "Banking_database"
                    ).addCallback(object : RoomDatabase.Callback() {  //Add initial dummy data to database (users data)
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                for (customer in customersList) {
                                    getInstance(context).customerDao.insertCustomer(customer)
                                    Log.v("BankDatabase : test", customer.firstName)
                                }
                            }
                        }
                    })
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance

                }
                return instance
            }

        }
    }

}