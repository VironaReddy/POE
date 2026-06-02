package com.example.practice.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {

    private val _budgets = mutableStateListOf<Budget>()
    val budgets: List<Budget> = _budgets

    fun addBudget(budget: Budget) {
        _budgets.add(budget)
    }

    fun getTotalBudget(): Double {
        return _budgets.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    }

    fun getCategoryTotal(category: String): Double {
        return _budgets
            .filter { it.category == category }
            .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    }
}