package com.example.fitnessapp.repository

import com.example.fitnessapp.model.BmiModel
import com.example.fitnessapp.model.CalorieModel
import com.example.fitnessapp.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun getUserById(userId: String, callback: (Boolean, UserModel?) -> Unit)

    fun getAllUser(callback: (Boolean, List<UserModel>?) -> Unit)

    fun getCurrentUser(): FirebaseUser?

    fun deleteUser(userId: String, callback: (Boolean, String) -> Unit)

    fun updateProfile(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )

    // New methods for security updates
    fun updateEmail(newEmail: String, callback: (Boolean, String) -> Unit)
    fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit)

    // BMI CRUD
    fun saveBmiData(model: BmiModel, callback: (Boolean, String) -> Unit)
    fun getBmiDataByUserId(userId: String, callback: (Boolean, List<BmiModel>?) -> Unit)
    fun deleteBmiData(bmiId: String, callback: (Boolean, String) -> Unit)

    // Calorie CRUD
    fun saveCalorieData(model: CalorieModel, callback: (Boolean, String) -> Unit)
    fun getCalorieDataByUserId(userId: String, callback: (Boolean, List<CalorieModel>?) -> Unit)
    fun deleteCalorieData(calorieId: String, callback: (Boolean, String) -> Unit)
}
