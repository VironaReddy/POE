package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        title = "My Budget Goals",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (budgets.isEmpty()) {
                Text(
                    "No spending goals set yet.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Text(
                            "Category Goals",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(budgets) { budget ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = budget.category,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text("Period: ${budget.dateRange}", style = MaterialTheme.typography.bodySmall)
                                
                                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                                
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("Min Goal", style = MaterialTheme.typography.labelSmall)
                                        Text("R ${budget.minLimit}", fontWeight = FontWeight.Medium)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Max Limit", style = MaterialTheme.typography.labelSmall)
                                        Text("R ${budget.maxLimit}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
                                    }
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
                Text("Set New Category Goal")
            }
        }
    }
}