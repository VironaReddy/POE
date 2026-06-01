package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice.data.IncomeCategoryRepository
import com.example.practice.data.IncomeRepository
import com.example.practice.model.Income
import com.example.practice.model.IncomeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    onSaveDone: () -> Unit = {},
    onAddCategory: () -> Unit = {}
) {

    val repo = remember { IncomeRepository() }
    val catRepo = remember { IncomeCategoryRepository() }

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<IncomeCategory?>(null) }
    var categories by remember { mutableStateOf(listOf<IncomeCategory>()) }

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        catRepo.getCategories { categories = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Add Income", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(title, { title = it }, label = { Text("Title") })

        Spacer(modifier = Modifier.height(10.dp))

        // 🔥 CATEGORY DROPDOWN
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

                // 🔥 ADD CATEGORY BUTTON
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
            repo.addIncome(
                Income(
                    title = title,
                    date = "",
                    amount = "",
                    description = selectedCategory?.title ?: ""
                )
            )
            onSaveDone()
        }) {
            Text("Save Income")
        }
    }
}