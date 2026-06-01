package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice.data.ExpenseCategoryRepository
import com.example.practice.data.ExpenseRepository
import com.example.practice.model.Expense
import com.example.practice.model.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onSaveDone: () -> Unit = {},
    onAddCategory: () -> Unit = {}
) {

    val repo = remember { ExpenseRepository() }
    val catRepo = remember { ExpenseCategoryRepository() }

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var categories by remember { mutableStateOf(listOf<ExpenseCategory>()) }

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        catRepo.getCategories {
            categories = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Add Expense", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // CATEGORY DROPDOWN
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = selectedCategory?.title ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("+ Add Category") },
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

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            repo.addExpense(
                Expense(
                    title = title,
                    date = "",
                    amount = "",
                    description = selectedCategory?.title ?: ""
                )
            )
            onSaveDone()
        }) {
            Text("Save Expense")
        }
    }
}