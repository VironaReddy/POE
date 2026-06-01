package com.example.practice.data

import com.example.practice.model.ExpenseCategory
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseCategoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("expenseCategories")

    fun addCategory(category: ExpenseCategory) {
        ref.add(category)
    }

    // ✅ FIXED: REAL-TIME LISTENER
    fun getCategories(onResult: (List<ExpenseCategory>) -> Unit) {
        ref.addSnapshotListener { snapshot, error ->

            if (error != null || snapshot == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = snapshot.documents.mapNotNull {
                it.toObject(ExpenseCategory::class.java)
            }

            onResult(list)
        }
    }
}