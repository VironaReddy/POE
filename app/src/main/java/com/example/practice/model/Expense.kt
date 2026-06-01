package com.example.practice.model

data class Expense(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val amount: String = "",
    val photoUri: String = ""
)