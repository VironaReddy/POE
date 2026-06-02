package com.example.practice.data

import com.example.practice.model.Budget
import com.google.firebase.firestore.FirebaseFirestore

class BudgetRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("budgets")

    fun addBudget(budget: Budget) {
        ref.add(budget)
    }

    fun getBudgets(onResult: (List<Budget>) -> Unit) {
        ref.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = snapshot.documents.mapNotNull {
                it.toObject(Budget::class.java)
            }
            onResult(list)
        }
    }
}