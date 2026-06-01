package com.example.practice.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.auth.AuthViewModel
import com.example.practice.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {

        composable("entry") {
            EntryScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("entry") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("home") {
            HomeScreen(authViewModel, navController)
        }

        composable("expenses") {
            ExpensesScreen(navController) { navController.navigate("addExpense") }
        }

        composable("addExpense") {
            AddExpenseScreen(
                onSaveDone = { navController.popBackStack() },
                onAddCategory = { navController.navigate("createExpenseCategory") }
            )
        }

        composable("createExpenseCategory") {
            CreateExpenseCategoryScreen { navController.popBackStack() }
        }

        composable("incomes") {
            IncomesScreen(navController) { navController.navigate("addIncome") }
        }

        composable("addIncome") {
            AddIncomeScreen(
                onSaveDone = { navController.popBackStack() },
                onAddCategory = { navController.navigate("createIncomeCategory") }
            )
        }

        composable("createIncomeCategory") {
            CreateIncomeCategoryScreen { navController.popBackStack() }
        }

        composable("budgets") {
            BudgetsScreen(navController) { navController.navigate("addBudget") }
        }

        composable("addBudget") {
            AddBudgetScreen(navController)
        }

        composable("reports") {
            Text("Reports Screen")
        }
    }
}