package com.example.practice.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomBar(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("expenses") },
            icon = { Icon(Icons.Default.Receipt, null) },
            label = { Text("Expenses") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("incomes") },
            icon = { Icon(Icons.Default.AttachMoney, null) },
            label = { Text("Income") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("budgets") },
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Budgets") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("reports") },
            icon = { Icon(Icons.Default.BarChart, null) },
            label = { Text("Reports") }
        )
    }
}