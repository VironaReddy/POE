package com.example.practice.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.BudgetRepository
import com.example.practice.data.ExpenseRepository
import com.example.practice.model.Budget
import com.example.practice.model.Expense
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val expenseRepo = remember { ExpenseRepository() }
    val budgetRepo = remember { BudgetRepository() }

    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    var budgets by remember { mutableStateOf(listOf<Budget>()) }
    var selectedMonth by remember { mutableStateOf("2024-10") }
    var expanded by remember { mutableStateOf(false) }

    val months = listOf("2024-09", "2024-10", "2024-11", "2024-12")

    LaunchedEffect(Unit) {
        expenseRepo.getExpenses { expenses = it }
        budgetRepo.getBudgets { budgets = it }
    }

    // Requirement: User-selectable period
    val filteredExpenses = expenses.filter { it.date.startsWith(selectedMonth) }
    
    // Group spending by category for the period
    val spendingByCategory = filteredExpenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } }

    ScreenWrapper(
        title = "Reports",
        navController = navController
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Select Reporting Period", style = MaterialTheme.typography.titleMedium)
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedMonth,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    months.forEach { month ->
                        DropdownMenuItem(
                            text = { Text(month) },
                            onClick = {
                                selectedMonth = month
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Spending Goals Analysis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Category totals vs Min/Max limits", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                // Display spending vs goals for each category that has a budget
                items(budgets) { budget ->
                    val spent = spendingByCategory[budget.category] ?: 0.0
                    SpendingGoalVisual(budget, spent)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Requirement: Total amount spent on each category
                // If a category has spending but no budget goal, show it too
                val categoriesWithBudgets = budgets.map { it.category }.toSet()
                val otherCategories = spendingByCategory.keys.filter { it !in categoriesWithBudgets }
                
                items(otherCategories) { category ->
                    val spent = spendingByCategory[category] ?: 0.0
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(category, fontWeight = FontWeight.Bold)
                            Text("Total Spent: R ${String.format("%.2f", spent)}")
                            Text("No spending goals set for this category.", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SpendingGoalVisual(budget: Budget, spent: Double) {
    val min = budget.minLimit.toDoubleOrNull() ?: 0.0
    val max = budget.maxLimit.toDoubleOrNull() ?: 1.0 // Prevent div by zero
    
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(budget.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("R ${String.format("%.2f", spent)}", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Visual Graph / Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                val displayMax = maxOf(max, spent) * 1.1
                val progress = (spent / displayMax).toFloat().coerceIn(0f, 1f)
                
                val barColor = when {
                    spent < min -> Color(0xFFFFA500) // Under min goal
                    spent <= max -> Color(0xFF4CAF50) // Safe zone
                    else -> Color.Red // Exceeded max
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(barColor, RoundedCornerShape(12.dp))
                )
                
                // Visual Indicator for Min Goal
                val minMarker = (min / displayMax).toFloat().coerceIn(0f, 1f)
                Box(modifier = Modifier.fillMaxWidth(minMarker).fillMaxHeight()) {
                   VerticalDivider(color = Color.Black.copy(alpha = 0.5f), thickness = 2.dp, modifier = Modifier.align(Alignment.CenterEnd))
                }

                // Visual Indicator for Max Limit
                val maxMarker = (max / displayMax).toFloat().coerceIn(0f, 1f)
                Box(modifier = Modifier.fillMaxWidth(maxMarker).fillMaxHeight()) {
                   VerticalDivider(color = Color.Black, thickness = 2.dp, modifier = Modifier.align(Alignment.CenterEnd))
                }
            }
            
            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Min: R $min", style = MaterialTheme.typography.labelSmall)
                Text("Max: R $max", style = MaterialTheme.typography.labelSmall)
            }
            
            val statusText = when {
                spent < min -> "Below Minimum Goal"
                spent <= max -> "Within Spending Goals"
                else -> "Exceeded Maximum Limit!"
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelMedium,
                color = if (spent > max) Color.Red else if (spent < min) Color(0xFFFFA500) else Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}