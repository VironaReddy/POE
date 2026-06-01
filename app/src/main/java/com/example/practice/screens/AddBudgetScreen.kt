package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.BudgetCategoryRepository
import com.example.practice.data.BudgetRepository
import com.example.practice.model.Budget
import com.example.practice.model.BudgetCategory
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    navController: NavController,
    onSaveDone: () -> Unit,
    onAddCategory: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dateRange by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<BudgetCategory?>(null) }
    var categories by remember { mutableStateOf(listOf<BudgetCategory>()) }
    var expanded by remember { mutableStateOf(false) }

    val catRepo = remember { BudgetCategoryRepository() }
    val budgetRepo = remember { BudgetRepository() }

    LaunchedEffect(Unit) {
        catRepo.getCategories { categories = it }
    }

    ScreenWrapper(
        title = "Add New Budget",
        navController = navController,
        showBottomBar = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = dateRange,
                onValueChange = { dateRange = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Budget Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = { /* Implement Upload logic */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Upload Document/Image")
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    budgetRepo.addBudget(
                        Budget(
                            title = name,
                            amount = amount,
                            category = selectedCategory?.title ?: "General",
                            description = description,
                            dateRange = dateRange
                        )
                    )
                    onSaveDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Budget")
            }
        }
    }
}