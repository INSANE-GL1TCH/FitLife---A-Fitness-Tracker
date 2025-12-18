package com.example.fitnessapp.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.repository.UserRepositoryImpl
import com.example.fitnessapp.ui.theme.FitLifeGreen
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.viewmodel.UserViewModel

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgetBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetBody() {
    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { data ->
                        email = data
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = {
                        Text("abc@gmail.com")
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = LightGray,
                        focusedContainerColor = LightGray,
                        focusedIndicatorColor = FitLifeGreen,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        userViewModel.forgetPassword(email) { success, message ->
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitLifeGreen
                    )
                ) {
                    Text("Forget Password")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPasswordPreview() {
    ForgetBody()
}
