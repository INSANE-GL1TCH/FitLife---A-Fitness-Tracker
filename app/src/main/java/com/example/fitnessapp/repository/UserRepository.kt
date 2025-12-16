package com.example.fitnessapp.repository

import com.example.fitnessapp.model.UserModel

interface UserRepository {
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit)
    fun register(
        email: String,
        password: String,
        callback: (Boolean, String?, String?) -> Unit
    )

    fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String?) -> Unit
    )
    suspend fun getUser(uid: String): UserModel?
}