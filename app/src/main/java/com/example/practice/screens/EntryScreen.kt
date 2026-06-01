package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Smart Spender", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onRegisterClick) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onLoginClick) {
            Text("Login")
        }
    }
}