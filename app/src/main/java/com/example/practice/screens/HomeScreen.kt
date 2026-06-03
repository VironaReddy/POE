package com.example.practice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practice.R
import com.example.practice.auth.AuthViewModel
import com.example.practice.data.ExpenseRepository
import com.example.practice.data.IncomeRepository
import com.example.practice.ui.components.ScreenWrapper

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val incomeRepo = remember { IncomeRepository() }
    val expenseRepo = remember { ExpenseRepository() }

    var totalIncome by remember { mutableStateOf(0.0) }
    var totalExpense by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        incomeRepo.getIncomes { list ->
            totalIncome = list.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        }
        expenseRepo.getExpenses { list ->
            totalExpense = list.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        }
    }

    ScreenWrapper(
        title = "Home",
        navController = navController,
        showBack = false
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hello there!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Welcome to smart spender",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                    
                    // Streak clickable icon
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streaks",
                        tint = Color(0xFFFF5722),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.navigate("streaks") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FancySquare(
                        title = "Income",
                        amount = totalIncome,
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.weight(1f)
                    )
                    FancySquare(
                        title = "Expense",
                        amount = totalExpense,
                        color = Color(0xFFFFEBEE),
                        modifier = Modifier.weight(1f)
                    )
                    FancySquare(
                        title = "Remaining",
                        amount = totalIncome - totalExpense,
                        color = Color(0xFFE3F2FD),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Check out all of our features",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Cursive,
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.pig),
                    contentDescription = "Savings Pig",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}

@Composable
fun FancySquare(
    title: String,
    amount: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "R${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
