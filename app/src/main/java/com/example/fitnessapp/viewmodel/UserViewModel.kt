package com.example.fitnessapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitnessapp.model.UserModel
import com.example.fitnessapp.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        userRepository.login(email, password, callback)
    }

    fun register(
        email: String,
        password: String,
        callback: (Boolean, String?, String?) -> Unit
    ) {
        userRepository.register(email, password, callback)
    }

    fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String?) -> Unit
    ) {
        userRepository.addUserToDatabase(userId, userModel, callback)
    }
}