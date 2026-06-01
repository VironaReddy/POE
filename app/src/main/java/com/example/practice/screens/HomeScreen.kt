package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.auth.AuthViewModel
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    ScreenWrapper(
        title = "Home",
        navController = navController
    ) {

        Text("Home", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            authViewModel.signout()
            navController.navigate("entry") {
                popUpTo(0)
            }
        }) {
            Text("Logout")
        }
    }
}