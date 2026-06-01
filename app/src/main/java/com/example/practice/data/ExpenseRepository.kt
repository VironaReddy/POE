package com.example.practice.data

import android.util.Log
import com.example.practice.model.Expense
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("expenses")

    fun addExpense(expense: Expense) {
        ref.add(expense)
            .addOnSuccessListener {
                Log.d("ExpenseRepo", "Expense saved successfully")
            }
            .addOnFailureListener {
                Log.e("ExpenseRepo", "Failed to save expense: ${it.message}")
            }
    }

    fun getExpenses(onResult: (List<Expense>) -> Unit) {
        ref.get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull {
                    it.toObject(Expense::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                Log.e("ExpenseRepo", "Failed to load expenses: ${it.message}")
                onResult(emptyList())
            }
    }
}