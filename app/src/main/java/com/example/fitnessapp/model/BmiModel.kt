package com.example.fitnessapp.model

import androidx.compose.ui.graphics.Color

data class BmiModel(
    val bmiId: String = "",
    val userId: String = "",
    val weight: String = "",
    val height: String = "",
    val bmi: String = "",
    val status: String = "",
    val date: String = ""
) {
    fun getStatusColor(): Color {
        return when (status) {
            "Normal" -> Color(0xFF008037) // FitLifeGreen
            "Underweight" -> Color(0xFF3498DB) // Blue
            "Overweight" -> Color(0xFFE67E22) // Orange
            "Obese" -> Color(0xFFE74C3C) // Red
            else -> Color.Gray
        }
    }
}
