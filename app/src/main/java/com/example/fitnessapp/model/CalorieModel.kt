package com.example.fitnessapp.model

data class CalorieModel(
    val calorieId: String = "",
    val userId: String = "",
    val mealName: String = "",
    val calorieGoal: String = "2000",
    val protein: String = "",
    val carbs: String = "",
    val fat: String = "",
    val totalCalories: String = "",
    val date: String = "" // Format: dd MMM yyyy
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "mealName" to mealName,
            "calorieGoal" to calorieGoal,
            "protein" to protein,
            "carbs" to carbs,
            "fat" to fat,
            "totalCalories" to totalCalories,
            "date" to date
        )
    }
}
