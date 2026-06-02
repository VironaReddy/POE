package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.ui.components.ScreenWrapper

data class ReminderDetails(
    val heading: String,
    val description: String,
    val date: String
)

@Composable
fun Reminder(navController: NavController) {
    val reminders = remember { mutableStateListOf<ReminderDetails>() }
    var date by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    ScreenWrapper(
        title = "Reminders",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Heading Input
            OutlinedTextField(
                value = heading,
                onValueChange = { heading = it },
                label = { Text("Heading") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date Input
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (dd/mm/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Set Reminder Button
            Button(
                onClick = {
                    if (heading.isNotBlank() && description.isNotBlank() && date.isNotBlank()) {
                        reminders.add(
                            ReminderDetails(
                                heading = heading,
                                description = description,
                                date = date
                            )
                        )
                        heading = ""
                        description = ""
                        date = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Reminder")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Active Reminders List
            Text(
                text = "Active Reminders",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(reminders) { reminder ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = reminder.heading, fontWeight = FontWeight.Bold)
                            Text(text = reminder.description)
                            Text(text = reminder.date, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
