package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.BudgetRepository
import com.example.practice.model.Budget
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun BudgetsScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { BudgetRepository() }
    var budgets by remember { mutableStateOf(listOf<Budget>()) }

    LaunchedEffect(Unit) {
        repo.getBudgets { budgets = it }
    }

    ScreenWrapper(
        title = "My Budget",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (budgets.isEmpty()) {
                Text(
                    "No budgets set yet",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(budgets) { budget ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Title: ${budget.title}", style = MaterialTheme.typography.titleMedium)
                                Text("Amount: R ${budget.amount}")
                                Text("Category: ${budget.category}")
                                Text("Period: ${budget.dateRange}")
                                if (budget.description.isNotBlank()) {
                                    Text("Description: ${budget.description}")
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text("Add New Budget")
            }
        }
    }
}