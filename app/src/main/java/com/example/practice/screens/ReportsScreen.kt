package com.example.practice.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var selectedMonth by remember { mutableStateOf("10/24") } // Matches DD/MM/YY pattern
    var expanded by remember { mutableStateOf(false) }

    val months = listOf("09/24", "10/24", "11/24", "12/24")
    
    // List state for scroll monitoring
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        // These repositories use addSnapshotListener, so they update automatically
        expenseRepo.getExpenses { expenses = it }
        budgetRepo.getBudgets { budgets = it }
    }

    // Filter expenses for the selected period
    val filteredExpenses = expenses.filter { it.date.endsWith(selectedMonth) }
    
    // Group daily spending for the Timeline Graph
    val dailySpending = filteredExpenses.groupBy { 
        it.date.split("/").firstOrNull() ?: "01" 
    }.mapValues { entry -> 
        entry.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } 
    }

    // Group spending by category for the Goal Analysis
    val spendingByCategory = filteredExpenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } }

    ScreenWrapper(
        title = "Financial Reports",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // PERIOD SELECTOR
                item {
                    Text("Select Reporting Period", style = MaterialTheme.typography.titleMedium)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = "Month: $selectedMonth",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            months.forEach { month ->
                                DropdownMenuItem(text = { Text(month) }, onClick = {
                                    selectedMonth = month
                                    expanded = false
                                })
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // DAILY TIMELINE GRAPH
                item {
                    Text("Daily Spending Timeline", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    SpendingTimelineGraph(dailySpending)
                    Spacer(Modifier.height(32.dp))
                }

                // SPENDING GOALS ANALYSIS
                item {
                    Text("Spending Goals Analysis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Category totals vs Min/Max goals", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                }

                if (budgets.isEmpty()) {
                    item {
                        Text("No budget goals set. Go to 'My Budgets' to set limits.", modifier = Modifier.padding(vertical = 10.dp))
                    }
                }

                items(budgets) { budget ->
                    val spent = spendingByCategory[budget.category] ?: 0.0
                    SpendingGoalVisual(budget, spent)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                // Show categories without budget goals
                val categoriesWithBudgets = budgets.map { it.category }.toSet()
                val otherCategories = spendingByCategory.keys.filter { it !in categoriesWithBudgets }
                
                if (otherCategories.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text("Other Category Spending", style = MaterialTheme.typography.titleMedium)
                    }
                    items(otherCategories) { category ->
                        val spent = spendingByCategory[category] ?: 0.0
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(category, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.weight(1f))
                                Text("Total: R ${String.format("%.2f", spent)}")
                            }
                        }
                    }
                }
            }

            // Visible Scrollbar indicator
            val scrollbarAlpha by remember {
                derivedStateOf { if (listState.isScrollInProgress) 1f else 0.4f }
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .fillMaxHeight(0.7f)
                    .width(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = scrollbarAlpha))
            )
        }
    }
}

@Composable
fun SpendingTimelineGraph(dailyData: Map<String, Double>) {
    val maxDaily = (dailyData.values.maxOrNull() ?: 1.0).coerceAtLeast(100.0)
    
    // Colorful palette for the bars
    val colors = listOf(
        Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFFF0266),
        Color(0xFFFDD835), Color(0xFF4CAF50), Color(0xFFFF9800),
        Color(0xFF2196F3), Color(0xFF9C27B0)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val barWidth = width / 31f // Max days in month
                
                dailyData.forEach { (day, amount) ->
                    val dayIdx = day.toIntOrNull() ?: 1
                    val x = (dayIdx - 1) * barWidth
                    val barHeight = (amount / maxDaily).toFloat() * height
                    
                    drawRect(
                        color = colors[dayIdx % colors.size],
                        topLeft = Offset(x + 2f, height - barHeight),
                        size = Size(barWidth - 4f, barHeight)
                    )
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(budget.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Spent: R ${String.format("%.2f", spent)}", fontWeight = FontWeight.Bold)
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
                    spent < min -> Color(0xFFFFA500) // Under min goal (Orange)
                    spent <= max -> Color(0xFF4CAF50) // Safe zone (Green)
                    else -> Color.Red // Exceeded max (Red)
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
