package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        title = "My Income",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (incomes.isEmpty()) {
                Text(
                    "No income yet",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Space for the button
                ) {
                    items(incomes) { income ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                Text("Title: ${income.title}", style = MaterialTheme.typography.titleMedium)
                                Text("Date: ${income.date}")
                                Text("Amount: ${income.amount}")
                                Text("Category: ${income.category}")
                            }
                        }
                    }
                }
            }

            // Button at the bottom above the NAV bar (NAV bar is in Scaffold via ScreenWrapper)
            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(androidx.compose.ui.Alignment.BottomCenter)
            ) {
                Text("Add New Income")
            }
        }
    }
}