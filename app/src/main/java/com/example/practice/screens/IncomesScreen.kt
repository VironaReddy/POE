package com.example.practice.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.practice.data.IncomeRepository
import com.example.practice.data.IncomeCategoryRepository
import com.example.practice.model.Income
import com.example.practice.model.IncomeCategory
import com.example.practice.ui.components.ScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesScreen(
    navController: NavController,
    onAddClick: () -> Unit
) {
    val repo = remember { IncomeRepository() }
    val catRepo = remember { IncomeCategoryRepository() }
    
    var allIncomes by remember { mutableStateOf(listOf<Income>()) }
    var categories by remember { mutableStateOf(listOf<IncomeCategory>()) }
    var selectedMonth by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val months = listOf("All", "10/24", "11/24", "12/24")
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        repo.getIncomes { allIncomes = it }
        catRepo.getCategories { categories = it }
    }

    val filteredIncomes = if (selectedMonth == "All") allIncomes else {
        allIncomes.filter { it.date.contains(selectedMonth) }
    }

    ScreenWrapper(
        title = "My Income",
        navController = navController
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(filteredIncomes) { income ->
                            val categoryIcon = categories.find { it.title == income.category }?.icon ?: "💰"
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(categoryIcon, style = MaterialTheme.typography.headlineSmall)
                                        Spacer(Modifier.width(12.dp))
                                        Column {
                                            Text(income.category, fontWeight = FontWeight.Bold)
                                            Text(income.description, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Spacer(Modifier.weight(1f))
                                        Text("R ${income.amount}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Text("Date: ${income.date}", style = MaterialTheme.typography.labelSmall)
                                    
                                    if (income.photoUri.isNotBlank()) {
                                        Spacer(Modifier.height(8.dp))
                                        Image(
                                            painter = rememberAsyncImagePainter(income.photoUri),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxWidth().height(120.dp),
                                            contentScale = ContentScale.Crop
                                        )
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
                            .padding(end = 4.dp, top = 20.dp, bottom = 80.dp)
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
                Text("Add New Income")
            }
        }
    }
}
