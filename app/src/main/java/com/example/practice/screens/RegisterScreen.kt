package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.practice.auth.AuthState
import com.example.practice.auth.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Error) {
            val msg = (authState.value as AuthState.Error).message
            if (msg == "Signup successful") {
                onRegisterSuccess()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            password,
            { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            authViewModel.signup(email, password)
        }) {
            Text("Create Account")
        }

        TextButton(onClick = onLoginClick) {
            Text("Back to Login")
        }
    }
}