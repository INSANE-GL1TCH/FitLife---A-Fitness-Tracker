package com.example.fitnessapp.model

data class UserModel(
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val contact: String = "",
    val dob: String = "",
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "contact" to contact,
            "dob" to dob
        )
    }
}
