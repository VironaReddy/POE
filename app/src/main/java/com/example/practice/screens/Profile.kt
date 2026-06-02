package com.example.practice.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practice.auth.AuthViewModel
import com.example.practice.model.User
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    user: User?
) {
    ScreenWrapper(
        title = "Profile",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = user?.username ?: "Guest",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (user?.phone?.isNotBlank() == true) {
                Text(
                    text = user.phone,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(20.dp))

            ProfileItem(
                title = "Settings",
                onClick = { /* TODO */ }
            )

            ProfileItem(
                title = "My Budget",
                onClick = { navController.navigate("budgets") }
            )

            ProfileItem(
                title = "My Rewards",
                onClick = { /* TODO */ }
            )

            HorizontalDivider()

            ProfileItem(
                title = "Logout",
                onClick = {
                    authViewModel.signout()
                    navController.navigate("entry") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}
