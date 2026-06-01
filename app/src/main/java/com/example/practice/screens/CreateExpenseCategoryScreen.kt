package com.example.practice.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.practice.data.ExpenseCategoryRepository
import com.example.practice.model.ExpenseCategory
import com.example.practice.ui.components.iconList
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun CreateExpenseCategoryScreen(
    onDone: () -> Unit
) {

    val repo = remember { ExpenseCategoryRepository() }

    var title by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<ImageVector?>(null) }
    var showPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Create Expense Category", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Category Title") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { showPicker = true }) {
            Text("Select Icon")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Selected: ${selectedIcon?.name ?: "None"}"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            repo.addCategory(
                ExpenseCategory(
                    title = title,
                    icon = selectedIcon?.name ?: ""
                )
            )
            onDone()
        }) {
            Text("Save Category")
        }
    }

    // ICON PICKER POPUP
    if (showPicker) {

        Dialog(onDismissRequest = { showPicker = false }) {

            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 8.dp
            ) {

                Column(modifier = Modifier.padding(10.dp)) {

                    Text("Select Icon")

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.height(300.dp)
                    ) {

                        items(iconList) { icon ->

                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(30.dp)
                                    .clickable {
                                        selectedIcon = icon
                                        showPicker = false
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(onClick = { showPicker = false }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}