package com.example.practice.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.automirrored.filled.ReceiptLong

@Composable
fun BottomBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "incomes",
            onClick = { navController.navigate("incomes") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.AttachMoney, null) },
            label = { Text("My Income") }
        )

        NavigationBarItem(
            selected = currentRoute == "expenses",
            onClick = { navController.navigate("expenses") { launchSingleTop = true } },
            icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, null) },
            label = { Text("My Expense") }
        )

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Homepage") }
        )

        NavigationBarItem(
            selected = currentRoute == "budgets",
            onClick = { navController.navigate("budgets") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("My Budget") }
        )

        NavigationBarItem(
            selected = currentRoute == "reports",
            onClick = { navController.navigate("reports") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.BarChart, null) },
            label = { Text("Reports") }
        )
    }
}