package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.auth.AuthViewModel
import com.example.practice.model.User
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    user: User?
) {
    // We use local state for the input fields
    var username by remember { mutableStateOf(user?.username ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    val email = user?.email ?: "" // Email is usually read-only for profile updates

    ScreenWrapper(
        title = "Edit Profile",
        navController = navController,
        showBottomBar = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "Update your details below", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Kept disabled as it's the login credential
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (user != null) {
                        val updatedUser = user.copy(
                            username = username,
                            phone = phone
                        )
                        authViewModel.updateUserProfile(updatedUser)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotBlank()
            ) {
                Text("Save Changes")
            }
        }
    }
}
