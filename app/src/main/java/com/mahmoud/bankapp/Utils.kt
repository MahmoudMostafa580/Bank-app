package com.mahmoud.bankapp

import com.mahmoud.bankapp.models.User


fun customersList(): List<User> {
    val usersList = mutableListOf<User>()
    usersList.add(User(0, "Customer", "1", "customer1@gmail.com", 3000.toDouble()))
    usersList.add(User(1, "Customer", "2", "customer2@gmail.com", 10000.toDouble()))
    usersList.add(User(2, "Customer", "3", "customer3@gmail.com", 5000.toDouble()))
    usersList.add(User(3, "Customer", "4", "customer4@gmail.com", 6000.toDouble()))
    usersList.add(User(4, "Customer", "5", "customer5@gmail.com", 7000.toDouble()))
    usersList.add(User(5, "Customer", "6", "customer6@gmail.com", 2500.toDouble()))
    usersList.add(User(6, "Customer", "7", "customer7@gmail.com", 5000.toDouble()))
    usersList.add(User(7, "Customer", "8", "customer8@gmail.com", 3000.toDouble()))
    usersList.add(User(8, "Customer", "9", "customer9@gmail.com", 4000.toDouble()))
    usersList.add(User(9, "Customer", "10", "customer10@gmail.com", 6500.toDouble()))

    return usersList
}
