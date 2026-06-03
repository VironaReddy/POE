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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practice.data.BudgetRepository
import com.example.practice.data.ExpenseRepository
import com.example.practice.model.Budget
import com.example.practice.model.Expense
import com.example.practice.ui.components.ScreenWrapper
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val expenseRepo = remember { ExpenseRepository() }
    val budgetRepo = remember { BudgetRepository() }

    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    var budgets by remember { mutableStateOf(listOf<Budget>()) }
    
    var selectedPeriod by remember { mutableStateOf("This Month") }
    var expanded by remember { mutableStateOf(false) }
    val periods = listOf("This Week", "This Month", "Last Month", "Custom Dates")
    
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        expenseRepo.getExpenses { expenses = it }
        budgetRepo.getBudgets { budgets = it }
    }

    val filteredExpenses = expenses.filter { 
        when(selectedPeriod) {
            "This Month" -> it.date.contains("/10/") 
            "Last Month" -> it.date.contains("/09/")
            else -> true
        }
    }

    val spendingByCategory = filteredExpenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 } }

    val minGoal = budgets.sumOf { it.minLimit.toDoubleOrNull() ?: 0.0 }.coerceAtLeast(1000.0)
    val maxGoal = budgets.sumOf { it.maxLimit.toDoubleOrNull() ?: 0.0 }.coerceAtLeast(3000.0)

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
                item {
                    Text("Select Reporting Period", style = MaterialTheme.typography.titleMedium)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedPeriod,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            periods.forEach { period ->
                                DropdownMenuItem(text = { Text(period) }, onClick = {
                                    selectedPeriod = period
                                    expanded = false
                                })
                            }
                        }
                    }
                    if (selectedPeriod == "Custom Dates") {
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = "01/06/2024", onValueChange = {}, label = { Text("Start") }, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = "30/06/2024", onValueChange = {}, label = { Text("End") }, modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    Text("📊 Category Spending Graph", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    
                    HorizontalCategorySpendingChart(spendingByCategory, minGoal, maxGoal)
                    
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    Text("Daily Spending Timeline", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    
                    val dailyData = filteredExpenses.groupBy { 
                        it.date.split("/").firstOrNull() ?: "01" 
                    }.mapValues { it.value.sumOf { e -> e.amount.toDoubleOrNull() ?: 0.0 } }
                    
                    TimelineSpendingChart(dailyData)
                    
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    Text("Summary Breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                }

                items(spendingByCategory.toList()) { (category, total) ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(category, fontWeight = FontWeight.Bold)
                            Text("R ${String.format(Locale.US, \"%.2f\", total)}")
                        }
                    }
                }
            }

            // Visible Scrollbar indicator
            val scrollbarAlpha by remember {
                derivedStateOf { if (listState.isScrollInProgress) 1f else 0.3f }
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp, top = 100.dp, bottom = 100.dp)
                    .fillMaxHeight()
                    .width(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.Gray.copy(alpha = 0.1f))
            ) {
                val thumbOffset by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        if (layoutInfo.totalItemsCount == 0) 0f
                        else listState.firstVisibleItemIndex.toFloat() / layoutInfo.totalItemsCount
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .graphicsLayer { translationY = thumbOffset * 400.dp.toPx() }
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = scrollbarAlpha), RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
fun HorizontalCategorySpendingChart(data: Map<String, Double>, minGoal: Double, maxGoal: Double) {
    if (data.isEmpty()) {
        Card(modifier = Modifier.fillMaxWidth().height(100.dp)) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Enter expenses to see data")
            }
        }
        return
    }

    val maxVal = (data.values.maxOrNull() ?: 0.0).coerceAtLeast(maxGoal) * 1.2
    val barColors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFFF0266), Color(0xFFFDD835), Color(0xFF4CAF50), Color(0xFFFF9800))

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val height = size.height
                    val width = size.width
                    
                    val maxLineX = (maxGoal / maxVal).toFloat() * width
                    val minLineX = (minGoal / maxVal).toFloat() * width
                    
                    drawLine(
                        color = Color.Red,
                        start = Offset(maxLineX, 0f),
                        end = Offset(maxLineX, height),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                        strokeWidth = 2.dp.toPx()
                    )
                    
                    drawLine(
                        color = Color.Blue,
                        start = Offset(minLineX, 0f),
                        end = Offset(minLineX, height),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    data.entries.forEachIndexed { index, entry ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text(entry.key.take(8), fontSize = 10.sp, modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold)
                            val barWidthFactor = (entry.value / maxVal).toFloat()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(barWidthFactor.coerceIn(0.01f, 1f))
                                    .height(24.dp)
                                    .background(barColors[index % barColors.size], RoundedCornerShape(4.dp))
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("R${entry.value.toInt()}", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Blue: Min Goal (R${minGoal.toInt()})", color = Color.Blue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Red: Max Goal (R${maxGoal.toInt()})", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TimelineSpendingChart(dailyData: Map<String, Double>) {
    val maxDaily = (dailyData.values.maxOrNull() ?: 1.0).coerceAtLeast(100.0)
    val colors = listOf(Color(0xFF673AB7), Color(0xFF00BCD4), Color(0xFFFF4081), Color(0xFFFFC107), Color(0xFF4CAF50), Color(0xFFFF5722))
    
    Card(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val barWidth = width / 31f 
                
                dailyData.forEach { (day, amount) ->
                    val dayIdx = day.toIntOrNull() ?: 1
                    val x = (dayIdx - 1) * barWidth
                    val barHeight = (amount / maxDaily).toFloat() * height
                    
                    drawRect(
                        color = colors[dayIdx % colors.size],
                        topLeft = Offset(x + 1f, height - barHeight),
                        size = Size(barWidth - 2f, barHeight)
                    )
                }
            }
        }
    }
}

@Composable
fun SpendingGoalVisual(budget: Budget, spent: Double) {
    val min = budget.minLimit.toDoubleOrNull() ?: 0.0
    val max = budget.maxLimit.toDoubleOrNull() ?: 1.0 
    
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(budget.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Spent: R ${String.format(Locale.US, \"%.2f\", spent)}", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                val displayMax = maxOf(max, spent) * 1.1
                val progress = (spent / displayMax).toFloat().coerceIn(0f, 1f)
                
                val barColor = when {
                    spent < min -> Color(0xFFFFA500) 
                    spent <= max -> Color(0xFF4CAF50) 
                    else -> Color.Red 
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(barColor, RoundedCornerShape(12.dp))
                )
                
                val minMarker = (min / displayMax).toFloat().coerceIn(0f, 1f)
                Box(modifier = Modifier.fillMaxWidth(minMarker).fillMaxHeight()) {
                   VerticalDivider(color = Color.Black.copy(alpha = 0.4f), thickness = 2.dp, modifier = Modifier.align(Alignment.CenterEnd))
                }

                val maxMarker = (max / displayMax).toFloat().coerceIn(0f, 1f)
                Box(modifier = Modifier.fillMaxWidth(maxMarker).fillMaxHeight()) {
                   VerticalDivider(color = Color.Black, thickness = 2.dp, modifier = Modifier.align(Alignment.CenterEnd))
                }
            }
        }
    }
}
