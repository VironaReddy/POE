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
        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("ExpenseRepo", "Listen failed: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull {
                    it.toObject(Expense::class.java)
                }
                onResult(list)
            }
        }
    }
}