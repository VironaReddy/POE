package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.ExpenseRepository
import com.example.practice.data.ExpenseCategoryRepository
import com.example.practice.model.Expense
import com.example.practice.model.ExpenseCategory
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    onSaveDone: () -> Unit,
    onAddCategory: () -> Unit
) {
    val repo = remember { ExpenseRepository() }
    val catRepo = remember { ExpenseCategoryRepository() }

    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var categories by remember { mutableStateOf(listOf<ExpenseCategory>()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        catRepo.getCategories { categories = it }
    }

    ScreenWrapper(
        title = "Add New Expense",
        navController = navController,
        showBottomBar = false // Hide bottom bar on entry forms to focus on input
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Upload Feature Placeholder
            OutlinedButton(onClick = { /* Implement Upload logic */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Upload Document/Image")
            }

            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.title ?: "Select Category",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("+ Create Category", color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            expanded = false
                            onAddCategory()
                        }
                    )
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.title) },
                            onClick = {
                                selectedCategory = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    repo.addExpense(Expense(
                        amount = amount,
                        date = date,
                        description = description,
                        category = selectedCategory?.title ?: "General",
                        title = description // Using description as title for simplicity
                    ))
                    onSaveDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Expense")
            }
        }
    }
}