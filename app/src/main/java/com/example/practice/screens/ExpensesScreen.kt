package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        title = "Expenses",
        navController = navController
    ) {

        Button(onClick = onAddClick) {
            Text("Add New Expense")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (expenses.isEmpty()) {
            Text("No expenses yet")
        } else {
            expenses.forEach { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Title: ${expense.title}")
                        Text("Date: ${expense.date}")
                        Text("Amount: ${expense.amount}")
                        Text("Description: ${expense.description}")
                    }
                }
            }
        }
    }
}