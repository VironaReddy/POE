package com.example.practice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.practice.data.ExpenseRepository
import com.example.practice.data.ExpenseCategoryRepository
import com.example.practice.model.Expense
import com.example.practice.model.ExpenseCategory
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { ExpenseRepository() }
    val catRepo = remember { ExpenseCategoryRepository() }
    
    var allExpenses by remember { mutableStateOf(listOf<Expense>()) }
    var categories by remember { mutableStateOf(listOf<ExpenseCategory>()) }
    var selectedMonth by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val months = listOf("All", "10/24", "11/24", "12/24")

    LaunchedEffect(Unit) {
        repo.getExpenses { allExpenses = it }
        catRepo.getCategories { categories = it }
    }

    val filteredExpenses = if (selectedMonth == "All") allExpenses else {
        allExpenses.filter { it.date.contains(selectedMonth) }
    }

    ScreenWrapper(
        title = "My Expense",
        navController = navController
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Month Filter
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = "Month: $selectedMonth",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter by Month") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
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

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredExpenses) { expense ->
                        val categoryIcon = categories.find { it.title == expense.category }?.icon ?: "💰"
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(categoryIcon, style = MaterialTheme.typography.headlineSmall)
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text(expense.category, fontWeight = FontWeight.Bold)
                                        Text(expense.description, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Spacer(Modifier.weight(1f))
                                    Text("R ${expense.amount}", fontWeight = FontWeight.ExtraBold)
                                }
                                Text("Date: ${expense.date} | Time: ${expense.startTime} - ${expense.endTime}", style = MaterialTheme.typography.labelSmall)
                                
                                if (expense.photoUri.isNotBlank()) {
                                    Spacer(Modifier.height(8.dp))
                                    Image(
                                        painter = rememberAsyncImagePainter(expense.photoUri),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth().height(120.dp),
                                        contentScale = ContentScale.Crop
                                    )
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
