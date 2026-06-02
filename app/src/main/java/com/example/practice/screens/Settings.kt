package com.example.practice.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsPage(navController: NavController) {

    var remindersEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))


        //Edit profile
        SettingsLinkItem(
            icon = Icons.Default.Person,
            text = "Edit Profile"
        ) {
            navController.navigate("edit_profile")
        }

        Spacer(modifier = Modifier.height(15.dp))

        //Password
        SettingsLinkItem(
            icon = Icons.Default.Lock,
            text = "Change Password / Set Biometrics"
        ) {
            navController.navigate("security")
        }

        Spacer(modifier = Modifier.height(15.dp))

        //Language
        SettingsLinkItem(
            icon = Icons.Default.Face,
            text = "Change Language"
        ) {
            navController.navigate("language")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reminders
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = "Reminders"
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text("Reminders")

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = remindersEnabled,
                onCheckedChange = {
                    remindersEnabled = it
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedButton(
                onClick = {
                    // Dark Mode
                }
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Dark Mode")
            }

            OutlinedButton(
                onClick = {
                    // Light Mode
                }
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Light Mode")
            }
        }
    }
}

@Composable
fun SettingsLinkItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = text
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            color = Color(0xFF5A5CFF),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}