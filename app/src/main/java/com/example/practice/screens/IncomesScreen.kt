package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.IncomeRepository
import com.example.practice.model.Income
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun IncomesScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {

    val repo = remember { IncomeRepository() }
    var incomes by remember { mutableStateOf(listOf<Income>()) }

    LaunchedEffect(Unit) {
        repo.getIncomes { incomes = it }
    }

    ScreenWrapper(
        title = "Income",
        navController = navController
    ) {

        Button(onClick = onAddClick) {
            Text("Add New Income")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (incomes.isEmpty()) {
            Text("No income yet")
        } else {
            incomes.forEach { income ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Title: ${income.title}")
                        Text("Date: ${income.date}")
                        Text("Amount: ${income.amount}")
                        Text("Description: ${income.description}")
                    }
                }
            }
        }
    }
}