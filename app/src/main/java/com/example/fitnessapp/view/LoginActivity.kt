package com.example.fitnessapp.view

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R
import com.example.fitnessapp.repository.UserRepositoryImpl
import com.example.fitnessapp.ui.theme.*
import com.example.fitnessapp.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
            LoginBody(userViewModel = userViewModel, onLoginSuccess = { finish() })
        }
    }
}

@Composable
fun LoginBody(userViewModel: UserViewModel?, onLoginSuccess: () -> Unit) {

    val keyCo = LocalSoftwareKeyboardController.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(LightGreen)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(painter = painterResource(id = R.drawable.img),
                contentDescription = "FitLife Logo",
                modifier = Modifier.size(150.dp).padding(bottom = 20.dp))

            Text("Sign In", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = FitLifeGreen)
            Text("Welcome!", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { data -> email = data },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                placeholder = { Text("example@gmail.com") },
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
                onValueChange = { data -> password = data },
                placeholder = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            imageVector = if (visibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
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

            Text(
                "Forgot your password?",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        val intent = Intent(context, ForgotPasswordActivity::class.java)
                        context.startActivity(intent)
                    },
                style = TextStyle(textAlign = TextAlign.End, color = FitLifeGreen, fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    keyCo?.hide()
                    userViewModel?.login(email, password) { success, message ->
                        if (success) {
                            val intent = Intent(context, DashboardActivity::class.java)
                            context.startActivity(intent)
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLifeGreen)
            ) {
                Text("Sign in", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Don\'t have an account? ")
                Text("Register", color = FitLifeGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    context.startActivity(intent)
                })
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Sign in with")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SocialMediaCard(icon = Icons.Default.Facebook)
                Spacer(modifier = Modifier.width(16.dp))
                SocialMediaCard(icon = Icons.Default.Email)
            }
        }
    }
}

@Composable
fun SocialMediaCard(icon: ImageVector) {
    Card(
        modifier = Modifier.size(50.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FitLifeGreen
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginBody(userViewModel = null, onLoginSuccess = {})
}
