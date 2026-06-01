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
import com.example.practice.data.ExpenseRepository
import com.example.practice.model.Expense
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun ExpensesScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { ExpenseRepository() }
    var expenses by remember { mutableStateOf(listOf<Expense>()) }

    LaunchedEffect(Unit) {
        repo.getExpenses { expenses = it }
    }

    ScreenWrapper(
        title = "My Expense",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (expenses.isEmpty()) {
                Text(
                    "No expenses yet",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(expenses) { expense ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Title: ${expense.title}", style = MaterialTheme.typography.titleMedium)
                                Text("Date: ${expense.date}")
                                Text("Amount: ${expense.amount}")
                                Text("Description: ${expense.description}")
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
                Text("Add New Expense")
            }
        }
    }
}