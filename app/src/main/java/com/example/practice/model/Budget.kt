package com.example.practice.model

data class Budget(
    val title: String = "",
    val category: String = "",
    val amount: String = "0",
    val minLimit: String = "0",
    val maxLimit: String = "0",
    val dateRange: String = "",
    val description: String = ""
)