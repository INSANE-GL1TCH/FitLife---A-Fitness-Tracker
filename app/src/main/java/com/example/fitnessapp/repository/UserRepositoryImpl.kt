package com.example.fitnessapp.repository

import com.example.fitnessapp.model.BmiModel
import com.example.fitnessapp.model.CalorieModel
import com.example.fitnessapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserRepositoryImpl : UserRepository {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userRef: DatabaseReference = database.getReference("Users")
    val bmiRef: DatabaseReference = database.getReference("BmiData")
    val calorieRef: DatabaseReference = database.getReference("CalorieData")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Login success")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Reset email sent to $email")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Registration success", "${auth.currentUser?.uid}")
                } else {
                    callback(false, "${it.exception?.message}", "")
                }
            }
    }

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        userRef.child(userId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User registered successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getUserById(
        userId: String,
        callback: (Boolean, UserModel?) -> Unit
    ) {
        userRef.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        callback(true, user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, null)
            }
        })
    }

    override fun getAllUser(callback: (Boolean, List<UserModel>?) -> Unit) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var allUsers = mutableListOf<UserModel>()
                    for (user in snapshot.children) {
                        val model = user.getValue(UserModel::class.java)
                        if (model != null) {
                            allUsers.add(model)
                        }
                    }
                    callback(true, allUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, emptyList())
            }
        })
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun deleteUser(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        userRef.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        userRef.child(userId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Profile updated successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateEmail(newEmail: String, callback: (Boolean, String) -> Unit) {
        auth.currentUser?.updateEmail(newEmail)?.addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Email updated in Auth")
            } else {
                callback(false, it.exception?.message ?: "Email update failed")
            }
        }
    }

    override fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit) {
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Password updated")
            } else {
                callback(false, it.exception?.message ?: "Password update failed")
            }
        }
    }

    // BMI CRUD
    override fun saveBmiData(model: BmiModel, callback: (Boolean, String) -> Unit) {
        val id = bmiRef.push().key.toString()
        val dataWithId = model.copy(bmiId = id)
        bmiRef.child(id).setValue(dataWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "BMI saved") else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun getBmiDataByUserId(userId: String, callback: (Boolean, List<BmiModel>?) -> Unit) {
        bmiRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BmiModel>()
                for (data in snapshot.children) {
                    data.getValue(BmiModel::class.java)?.let { list.add(it) }
                }
                callback(true, list)
            }
            override fun onCancelled(error: DatabaseError) = callback(false, null)
        })
    }

    override fun deleteBmiData(bmiId: String, callback: (Boolean, String) -> Unit) {
        bmiRef.child(bmiId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "BMI record deleted") else callback(false, it.exception?.message ?: "Error")
        }
    }

    // Calorie CRUD
    override fun saveCalorieData(model: CalorieModel, callback: (Boolean, String) -> Unit) {
        val id = calorieRef.push().key.toString()
        val dataWithId = model.copy(calorieId = id)
        calorieRef.child(id).setValue(dataWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Calories saved") else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun getCalorieDataByUserId(userId: String, callback: (Boolean, List<CalorieModel>?) -> Unit) {
        calorieRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CalorieModel>()
                for (data in snapshot.children) {
                    data.getValue(CalorieModel::class.java)?.let { list.add(it) }
                }
                callback(true, list)
            }
            override fun onCancelled(error: DatabaseError) = callback(false, null)
        })
    }

    override fun deleteCalorieData(calorieId: String, callback: (Boolean, String) -> Unit) {
        calorieRef.child(calorieId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Calorie record deleted") else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun updateCalorieData(calorieId: String, model: CalorieModel, callback: (Boolean, String) -> Unit) {
        calorieRef.child(calorieId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Meal updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Update failed")
            }
        }
    }
}
