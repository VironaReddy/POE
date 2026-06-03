package com.example.practice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practice.R
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
        navController = navController,
        showBottomBar = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name and Email
            Text(
                text = user?.username ?: "Johnson King",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user?.email ?: "jhoking@gmail.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Edit Profile Button
            Button(
                onClick = { navController.navigate("edit_profile") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008080)),
                modifier = Modifier.height(40.dp).padding(horizontal = 32.dp)
            ) {
                Text("Edit Profile", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menu Items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    onClick = { navController.navigate("settings") }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Wallet,
                    title = "My Budget",
                    onClick = { navController.navigate("budgets") }
                )
                ProfileMenuItem(
                    icon = Icons.Default.WorkspacePremium,
                    title = "My Rewards",
                    onClick = { navController.navigate("rewards") }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Change Password",
                    onClick = { /* Navigate to Change Password */ }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfileMenuItem(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Help & Support",
                    onClick = { /* Navigate to Help */ }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Log out",
                    iconColor = Color(0xFF9C27B0),
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
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color(0xFF9C27B0),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Black
        )
    }
}
