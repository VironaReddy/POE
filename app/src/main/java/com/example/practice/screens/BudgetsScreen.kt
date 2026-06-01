package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun BudgetsScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {

    ScreenWrapper(
        title = "Budgets",
        navController = navController
    ) {

        Text("My Budgets")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onAddClick) {
            Text("Add New Budget")
        }
    }
}