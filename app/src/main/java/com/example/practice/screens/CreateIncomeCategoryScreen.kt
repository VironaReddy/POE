package com.example.practice.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.IncomeCategoryRepository
import com.example.practice.model.IncomeCategory
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun CreateIncomeCategoryScreen(
    navController: NavController
) {
    val repo = remember { IncomeCategoryRepository() }
    var title by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("💰") }
    var categories by remember { mutableStateOf(emptyList<IncomeCategory>()) }
    
    val icons = listOf("💰", "📈", "🏦", "💼", "💵", "🪙", "💹", "🎁", "🧧", "💎")

    LaunchedEffect(Unit) {
        repo.getCategories { categories = it }
    }

    ScreenWrapper(
        title = "Create Income Category",
        navController = navController,
        showBottomBar = false
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select an Icon", style = MaterialTheme.typography.titleMedium)
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 50.dp),
                modifier = Modifier.height(120.dp).padding(vertical = 8.dp)
            ) {
                items(icons) { icon ->
                    FilterChip(
                        selected = selectedIcon == icon,
                        onClick = { selectedIcon = icon },
                        label = { Text(icon, style = MaterialTheme.typography.headlineSmall) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        repo.addCategory(IncomeCategory(title = title, icon = selectedIcon))
                        title = "" // Clear after adding
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Category")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Existing Categories", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(categories) { cat ->
                    ListItem(
                        headlineContent = { Text(cat.title) },
                        leadingContent = { Text(cat.icon, style = MaterialTheme.typography.headlineSmall) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}