package com.example.fitnessapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R
import com.example.fitnessapp.model.UserModel
import com.example.fitnessapp.repository.UserRepositoryImpl
import com.example.fitnessapp.ui.theme.*
import com.example.fitnessapp.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
            RegisterBody(userViewModel = userViewModel, onRegistrationSuccess = { finish() })
        }
    }
}

@Composable
fun RegisterBody(userViewModel: UserViewModel?, onRegistrationSuccess: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var confirmVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(LightGreen)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // Improvement 1: Added imePadding
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "FitLife Logo",
                modifier = Modifier.size(150.dp).padding(bottom = 20.dp))

            Text("Create Account", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
            Text("Sign up!", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                placeholder = { Text("Full Name") },
                singleLine = true, // Improvement 2: Set singleLine
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = LightGray,
                    focusedContainerColor = LightGray,
                    focusedIndicatorColor = FitLifeGreen,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                placeholder = { Text("example@gmail.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Improvement 3: Email Keyboard
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = LightGray,
                    focusedContainerColor = LightGray,
                    focusedIndicatorColor = FitLifeGreen,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(imageVector = if (visibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Improvement 4: Password Keyboard
                visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = LightGray,
                    focusedContainerColor = LightGray,
                    focusedIndicatorColor = FitLifeGreen,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { confirmVisibility = !confirmVisibility }) {
                        Icon(imageVector = if (confirmVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Improvement 5: Password Keyboard
                visualTransformation = if (!confirmVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = LightGray,
                    focusedContainerColor = LightGray,
                    focusedIndicatorColor = FitLifeGreen,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        userViewModel?.register(email, password) { success, message, userId ->
                            if (success) {
                                val model = UserModel(userId = userId.orEmpty(), email = email, firstName = fullName)
                                userViewModel.addUserToDatabase(userId.orEmpty(), model) { addSuccess, addMessage ->
                                    if (addSuccess) {
                                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        onRegistrationSuccess()
                                    } else {
                                        Toast.makeText(context, addMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen)
            ) {
                Text("Sign Up", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Already have an account? ")
                Text("Sign In", color = FitLifeGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onRegistrationSuccess() })
            }
        }
    }
}
