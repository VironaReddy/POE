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
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var minLimit by remember { mutableStateOf("") }
    var maxLimit by remember { mutableStateOf("") }
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
        title = "Set Spending Goals",
        navController = navController,
        showBottomBar = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Define your spending goals for a category.", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Budget Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                            text = { Text("${cat.icon} ${cat.title}") },
                            onClick = {
                                selectedCategory = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Total Budget Amount (R)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = minLimit,
                    onValueChange = { minLimit = it },
                    label = { Text("Min Goal") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = maxLimit,
                    onValueChange = { maxLimit = it },
                    label = { Text("Max Limit") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dateRange,
                onValueChange = { dateRange = it },
                label = { Text("Period (e.g. 2024-10)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedCategory != null) {
                        budgetRepo.addBudget(
                            Budget(
                                title = title,
                                category = selectedCategory!!.title,
                                amount = amount,
                                minLimit = minLimit,
                                maxLimit = maxLimit,
                                dateRange = dateRange,
                                description = description
                            )
                        )
                        onSaveDone()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && amount.isNotBlank() && selectedCategory != null
            ) {
                Text("Save Budget Goal")
            }
        }
    }
}