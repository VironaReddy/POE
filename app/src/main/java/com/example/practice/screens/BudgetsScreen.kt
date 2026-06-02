package com.example.practice.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.data.BudgetRepository
import com.example.practice.data.BudgetCategoryRepository
import com.example.practice.model.Budget
import com.example.practice.model.BudgetCategory
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun BudgetsScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { BudgetRepository() }
    val catRepo = remember { BudgetCategoryRepository() }
    
    var budgets by remember { mutableStateOf(listOf<Budget>()) }
    var categories by remember { mutableStateOf(listOf<BudgetCategory>()) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        repo.getBudgets { budgets = it }
        catRepo.getCategories { categories = it }
    }

    ScreenWrapper(
        title = "My Budgets",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(budgets) { budget ->
                            val categoryIcon = categories.find { it.title == budget.category }?.icon ?: "📊"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(categoryIcon, style = MaterialTheme.typography.headlineSmall)
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            text = budget.category,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Spacer(Modifier.height(8.dp))
                                    Text("Budget Amount: R ${budget.amount}", style = MaterialTheme.typography.bodyMedium)
                                    
                                    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Min Goal: R ${budget.minLimit}", style = MaterialTheme.typography.bodySmall)
                                        Text("Max Goal: R ${budget.maxLimit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                    }
                                    
                                    if (budget.description.isNotBlank()) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(text = budget.description, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }

                    // --- Scrollbar implementation ---
                    val scrollbarAlpha by remember {
                        derivedStateOf { if (listState.isScrollInProgress) 1f else 0.3f }
                    }
                    
                    val scrollThumbHeightWeight by remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            if (layoutInfo.totalItemsCount == 0) 1f
                            else layoutInfo.visibleItemsInfo.size.toFloat() / layoutInfo.totalItemsCount
                        }
                    }

                    val scrollThumbOffsetWeight by remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            if (layoutInfo.totalItemsCount == 0) 0f
                            else listState.firstVisibleItemIndex.toFloat() / layoutInfo.totalItemsCount
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp, top = 20.dp, bottom = 20.dp)
                            .fillMaxHeight()
                            .width(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.Gray.copy(alpha = 0.1f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(scrollThumbHeightWeight)
                                .graphicsLayer { translationY = scrollThumbOffsetWeight * size.height * (1 / scrollThumbHeightWeight) }
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = scrollbarAlpha), RoundedCornerShape(3.dp))
                        )
                    }
                }
            }

            Button(
                onClick = onAddClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text("Set New Category Goal")
            }
        }
    }
}
