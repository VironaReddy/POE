package com.example.practice.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.practice.data.ExpenseRepository
import com.example.practice.model.Expense
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { ExpenseRepository() }
    var allExpenses by remember { mutableStateOf(listOf<Expense>()) }
    var selectedMonth by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val months = listOf("All", "2024-10", "2024-11", "2024-12") // Simplified period selector

    LaunchedEffect(Unit) {
        repo.getExpenses { allExpenses = it }
    }

    val filteredExpenses = if (selectedMonth == "All") {
        allExpenses
    } else {
        allExpenses.filter { it.date.startsWith(selectedMonth) }
    }

    ScreenWrapper(
        title = "My Expense",
        navController = navController
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Requirement: User-selectable period
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = "Period: $selectedMonth",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter by Period") },
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

            Box(modifier = Modifier.weight(1f)) {
                if (filteredExpenses.isEmpty()) {
                    Text("No expenses for this period", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredExpenses) { expense ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text(expense.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                        Text("R ${expense.amount}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                    }
                                    Text(expense.description, style = MaterialTheme.typography.titleMedium)
                                    Text("Date: ${expense.date} | Time: ${expense.startTime} - ${expense.endTime}", style = MaterialTheme.typography.bodySmall)

                                    // Requirement: Access photo if stored
                                    if (expense.photoUri.isNotBlank()) {
                                        Spacer(Modifier.height(8.dp))
                                        Image(
                                            painter = rememberAsyncImagePainter(expense.photoUri),
                                            contentDescription = "Expense Photo",
                                            modifier = Modifier.fillMaxWidth().height(150.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.BottomCenter)
                ) {
                    Text("Add New Expense")
                }
            }
        }
    }
}