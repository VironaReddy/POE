package com.example.practice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.auth.AuthViewModel
import com.example.practice.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel() }

    // App starts on "entry" as defined by startDestination
    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {
        // ENTRY SCREEN
        composable("entry") {
            EntryScreen(
                navController = navController,
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // LOGIN SCREEN
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("entry") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        // REGISTER SCREEN
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                navController = navController,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("entry") { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate("login") }
            )
        }

        // HOMEPAGE
        composable("home") { 
            HomeScreen(authViewModel, navController) 
        }

        // MY EXPENSE
        composable("expenses") {
            ExpensesScreen(navController) { 
                navController.navigate("add_expense") 
            }
        }
        composable("add_expense") {
            AddExpenseScreen(
                navController = navController,
                onSaveDone = { navController.popBackStack() },
                onAddCategory = { navController.navigate("create_expense_category") }
            )
        }
        composable("create_expense_category") {
            CreateExpenseCategoryScreen(navController)
        }

        // MY INCOME
        composable("incomes") {
            IncomesScreen(navController) { 
                navController.navigate("add_income") 
            }
        }
        composable("add_income") {
            AddIncomeScreen(
                navController = navController,
                onSaveDone = { navController.popBackStack() },
                onAddCategory = { navController.navigate("create_income_category") }
            )
        }
        composable("create_income_category") {
            CreateIncomeCategoryScreen(navController)
        }

        // MY BUDGET
        composable("budgets") {
            BudgetsScreen(navController) { 
                navController.navigate("add_budget") 
            }
        }
        composable("add_budget") {
            AddBudgetScreen(
                navController = navController,
                onSaveDone = { navController.popBackStack() },
                onAddCategory = { navController.navigate("create_budget_category") }
            )
        }
        composable("create_budget_category") {
            CreateBudgetCategoryScreen(navController)
        }

        // REPORTS
        composable("reports") {
            ReportsScreen(navController)
        }
    }
}