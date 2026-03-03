package com.example.fitnessapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.model.BmiModel
import com.example.fitnessapp.model.CalorieModel
import com.example.fitnessapp.model.UserModel
import com.example.fitnessapp.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo: UserRepository) : ViewModel() {

    fun login(
        email: String, password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.login(email, password, callback)
    }

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        repo.register(email, password, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addUserToDatabase(userId, model, callback)
    }

    private val _users = MutableLiveData<UserModel?>()
    val users: MutableLiveData<UserModel?>
        get() = _users

    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: MutableLiveData<List<UserModel>?>
        get() = _allUsers

    private val _bmiData = MutableLiveData<List<BmiModel>?>()
    val bmiData: MutableLiveData<List<BmiModel>?>
        get() = _bmiData

    private val _calorieData = MutableLiveData<List<CalorieModel>?>()
    val calorieData: MutableLiveData<List<CalorieModel>?>
        get() = _calorieData

    fun getUserById(userId: String) {
        repo.getUserById(userId) {
            success, user ->
            if (success) {
                _users.postValue(user)
            }
        }
    }

    fun getAllUser() {
        repo.getAllUser {
            success, data ->
            if (success) {
                _allUsers.postValue(data)
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    fun deleteUser(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteUser(userId, callback)
    }

    fun updateProfile(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateProfile(userId, model, callback)
    }

    fun updateEmail(newEmail: String, callback: (Boolean, String) -> Unit) {
        repo.updateEmail(newEmail, callback)
    }

    fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit) {
        repo.updatePassword(newPassword, callback)
    }

    // BMI CRUD
    fun saveBmiData(model: BmiModel, callback: (Boolean, String) -> Unit) {
        repo.saveBmiData(model, callback)
    }

    fun getBmiDataByUserId(userId: String) {
        repo.getBmiDataByUserId(userId) { success, data ->
            if (success) _bmiData.postValue(data)
        }
    }

    fun deleteBmiData(bmiId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteBmiData(bmiId, callback)
    }

    // Calorie CRUD
    fun saveCalorieData(model: CalorieModel, callback: (Boolean, String) -> Unit) {
        repo.saveCalorieData(model, callback)
    }

    fun getCalorieDataByUserId(userId: String) {
        repo.getCalorieDataByUserId(userId) { success, data ->
            if (success) _calorieData.postValue(data)
        }
    }

    fun deleteCalorieData(calorieId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteCalorieData(calorieId, callback)
    }

    fun updateCalorieData(calorieId: String, model: CalorieModel, callback: (Boolean, String) -> Unit) {
        repo.updateCalorieData(calorieId, model, callback)
    }
}
