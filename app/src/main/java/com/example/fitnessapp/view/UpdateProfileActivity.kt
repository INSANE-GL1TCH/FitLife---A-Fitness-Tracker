package com.example.fitnessapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.model.UserModel
import com.example.fitnessapp.repository.UserRepositoryImpl
import com.example.fitnessapp.ui.theme.FitLifeGreen
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.viewmodel.UserViewModel

class UpdateProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
            UpdateProfileScreen(userViewModel, onBack = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(userViewModel: UserViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val currentUser = userViewModel.getCurrentUser()
    val userData by userViewModel.users.observeAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userViewModel.getUserById(it) }
    }

    LaunchedEffect(userData) {
        userData?.let {
            name = it.firstName
            email = it.email
            contact = it.contact
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = FitLifeGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = FitLifeGreen)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .imePadding() // Change 1: Added imePadding for better keyboard handling
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Personal Information", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                singleLine = true, // Change 2: Set singleLine to true
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact Number") },
                singleLine = true, // Change 3: Set singleLine to true
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(thickness = 1.dp, color = LightGray) // Change 4: Replaced Divider with HorizontalDivider
            Spacer(modifier = Modifier.height(32.dp))

            Text("Security Settings", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true, // Change 5: Set singleLine to true
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                placeholder = { Text("Leave blank to keep current") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val userId = currentUser?.uid ?: ""
                    
                    val updatedModel = UserModel(
                        userId = userId,
                        email = email,
                        firstName = name,
                        contact = contact
                    )
                    
                    userViewModel.updateProfile(userId, updatedModel) { success, message ->
                        if (success) {
                            if (email != currentUser?.email) {
                                userViewModel.updateEmail(email) { emailSuccess, emailMsg ->
                                    if (!emailSuccess) Toast.makeText(context, emailMsg, Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            if (newPassword.isNotEmpty()) {
                                userViewModel.updatePassword(newPassword) { passSuccess, passMsg ->
                                    if (!passSuccess) Toast.makeText(context, passMsg, Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            onBack()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save All Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
