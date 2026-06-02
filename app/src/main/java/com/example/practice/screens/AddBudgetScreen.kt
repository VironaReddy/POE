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
    var budgetAmount by remember { mutableStateOf("") }
    var minGoal by remember { mutableStateOf("") }
    var maxGoal by remember { mutableStateOf("") }
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
        title = "Set Budget Goal",
        navController = navController,
        showBottomBar = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Set your spending goals per category.", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection (dropdown add new and display made one name and icon)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.let { "${it.icon} ${it.title}" } ?: "Select Category",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("+ Add New Category", color = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            expanded = false
                            onAddCategory()
                        }
                    )
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text("${cat.icon} ${cat.title}") },
                            onClick = {
                                selectedCategory = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = budgetAmount,
                onValueChange = { budgetAmount = it },
                label = { Text("Budget Amount (R)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = minGoal,
                    onValueChange = { minGoal = it },
                    label = { Text("Min Goal (R)") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = maxGoal,
                    onValueChange = { maxGoal = it },
                    label = { Text("Max Goal (R)") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (selectedCategory != null) {
                        budgetRepo.addBudget(
                            Budget(
                                category = selectedCategory!!.title,
                                amount = budgetAmount,
                                minLimit = minGoal,
                                maxLimit = maxGoal,
                                description = description
                            )
                        )
                        onSaveDone()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = budgetAmount.isNotBlank() && selectedCategory != null
            ) {
                Text("Save Budget Goal")
            }
        }
    }
}
