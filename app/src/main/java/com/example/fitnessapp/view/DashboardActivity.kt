package com.example.fitnessapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.model.BmiModel
import com.example.fitnessapp.model.CalorieModel
import com.example.fitnessapp.repository.UserRepositoryImpl
import com.example.fitnessapp.ui.theme.FitLifeGreen
import com.example.fitnessapp.ui.theme.LightGreen
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
            DashboardScreen(userViewModel)
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun DashboardScreen(userViewModel: UserViewModel?) {
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Exercise", "exercise", Icons.Default.FitnessCenter),
        BottomNavItem("Progress", "progress", Icons.Default.History),
        BottomNavItem("Profile", "profile", Icons.Default.Person)
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FitLifeGreen,
                            selectedTextColor = FitLifeGreen,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = LightGreen
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(LightGray)) {
            if (userViewModel != null) {
                when (selectedItem) {
                    0 -> HomeScreen(userViewModel)
                    1 -> ExerciseScreen(userViewModel)
                    2 -> ProgressScreen(userViewModel)
                    3 -> ProfileScreen(
                        onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        onUpdateProfile = {
                            val intent = Intent(context, UpdateProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            } else {
                Text("UserViewModel is null")
            }
        }
    }
}

@Composable
fun HomeScreen(userViewModel: UserViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var calorieGoal by remember { mutableStateOf("2000") }

    val w = weight.toDoubleOrNull() ?: 0.0
    val h = (height.toDoubleOrNull() ?: 0.0) / 100.0
    val calculatedBmi = if (h > 0) w / (h * h) else 0.0
    
    val p = protein.toDoubleOrNull() ?: 0.0
    val c = carbs.toDoubleOrNull() ?: 0.0
    val f = fat.toDoubleOrNull() ?: 0.0
    val calculatedCalories = (p * 4) + (c * 4) + (f * 9)
    val goal = calorieGoal.toDoubleOrNull() ?: 2000.0
    val progress = if (goal > 0) (calculatedCalories / goal).coerceIn(0.0, 1.0).toFloat() else 0f

    val bmiStatus = when {
        calculatedBmi <= 0 -> ""
        calculatedBmi < 18.5 -> "Underweight"
        calculatedBmi < 25 -> "Normal"
        calculatedBmi < 30 -> "Overweight"
        else -> "Obese"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fitness Tracker", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("BMI Calculator", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) weight = it },
                        label = { Text("Weight (kg)") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = height,
                        onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) height = it },
                        label = { Text("Height (cm)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                if (calculatedBmi > 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("BMI: ${"%.2f".format(calculatedBmi)} ($bmiStatus)", fontWeight = FontWeight.Bold, color = if (bmiStatus == "Normal") FitLifeGreen else Color.Red)
                    Button(
                        onClick = {
                            val userId = userViewModel.getCurrentUser()?.uid ?: ""
                            val date = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
                            val model = BmiModel(userId = userId, weight = weight, height = height, bmi = "%.2f".format(calculatedBmi), status = bmiStatus, date = date)
                            userViewModel.saveBmiData(model) { success, msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                        },
                        modifier = Modifier.padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen)
                    ) { Text("Save BMI") }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Calorie Tracker", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
                OutlinedTextField(
                    value = calorieGoal,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) calorieGoal = it },
                    label = { Text("Goal") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row {
                    OutlinedTextField(value = protein, onValueChange = { protein = it }, label = { Text("P") }, modifier = Modifier.weight(1f).padding(end = 4.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = carbs, onValueChange = { carbs = it }, label = { Text("C") }, modifier = Modifier.weight(1f).padding(end = 4.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("F") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(8.dp), color = if (calculatedCalories <= goal) FitLifeGreen else Color.Red)
                Text("${calculatedCalories.toInt()} / ${goal.toInt()} kcal", modifier = Modifier.padding(top = 4.dp))
                Button(
                    onClick = {
                        val userId = userViewModel.getCurrentUser()?.uid ?: ""
                        val date = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
                        val model = CalorieModel(userId = userId, calorieGoal = calorieGoal, protein = protein, carbs = carbs, fat = fat, totalCalories = calculatedCalories.toInt().toString(), date = date)
                        userViewModel.saveCalorieData(model) { success, msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                    },
                    modifier = Modifier.padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen)
                ) { Text("Save Calories") }
            }
        }
    }
}

@Composable
fun ExerciseScreen(userViewModel: UserViewModel) {
    val bmiHistory by userViewModel.bmiData.observeAsState(emptyList())
    val lastBmi = bmiHistory?.lastOrNull()
    val status = lastBmi?.status ?: "Unknown"

    val exercises = when (status) {
        "Underweight" -> listOf("Push-ups", "Squats", "Lunges", "Bench Press", "Deadlifts")
        "Normal" -> listOf("Running", "Swimming", "Yoga", "Cycling", "Plank")
        "Overweight" -> listOf("Brisk Walking", "Jumping Jacks", "Burpees", "Mountain Climbers", "Zumba")
        "Obese" -> listOf("Slow Walking", "Water Aerobics", "Seated Leg Lifts", "Wall Push-ups", "Tai Chi")
        else -> listOf("Save your BMI on the Home tab to get recommendations.")
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Exercise Recommendations", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Based on your last status: $status", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = FitLifeGreen)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(exercise, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressScreen(userViewModel: UserViewModel) {
    val currentUser = userViewModel.getCurrentUser()
    val bmiHistoryState = userViewModel.bmiData.observeAsState(emptyList())
    val calorieHistoryState = userViewModel.calorieData.observeAsState(emptyList())
    
    val bmiHistory = bmiHistoryState.value
    val calorieHistory = calorieHistoryState.value

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { 
            userViewModel.getBmiDataByUserId(it)
            userViewModel.getCalorieDataByUserId(it)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("BMI History", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen) }
        if (bmiHistory.isNullOrEmpty()) {
            item { Text("No BMI records found.", color = Color.Gray, fontSize = 14.sp) }
        } else {
            items(bmiHistory.reversed()) { data ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(data.date, fontSize = 12.sp, color = Color.Gray)
                            Text("BMI: ${data.bmi} (${data.status})", fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { userViewModel.deleteBmiData(data.bmiId) { _, _ -> userViewModel.getBmiDataByUserId(currentUser?.uid ?: "") } }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Text("Calorie History", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen) }
        if (calorieHistory.isNullOrEmpty()) {
            item { Text("No calorie records found.", color = Color.Gray, fontSize = 14.sp) }
        } else {
            items(calorieHistory.reversed()) { data ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(data.date, fontSize = 12.sp, color = Color.Gray)
                            Text("${data.totalCalories} kcal (Goal: ${data.calorieGoal})", fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { userViewModel.deleteCalorieData(data.calorieId) { _, _ -> userViewModel.getCalorieDataByUserId(currentUser?.uid ?: "") } }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(onLogout: () -> Unit, onUpdateProfile: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(100.dp), tint = FitLifeGreen)
        Text("User Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onUpdateProfile, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen), shape = RoundedCornerShape(12.dp)) { Text("Update Profile") }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) { Text("Logout", color = Color.Red) }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() { DashboardScreen(userViewModel = null) }
