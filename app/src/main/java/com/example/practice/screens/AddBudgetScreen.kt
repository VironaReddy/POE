package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun AddBudgetScreen(
    navController: NavController
) {

    ScreenWrapper(
        title = "Add Budget",
        navController = navController
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Budget Name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Amount") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { }) {
            Text("Save Budget")
        }
    }
}