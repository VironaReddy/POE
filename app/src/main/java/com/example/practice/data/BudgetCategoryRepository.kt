package com.example.practice.data

import com.example.practice.model.BudgetCategory
import com.google.firebase.firestore.FirebaseFirestore

class BudgetCategoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("budgetCategories")

    fun addCategory(category: BudgetCategory) {
        ref.add(category)
    }

    fun getCategories(onResult: (List<BudgetCategory>) -> Unit) {
        ref.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = snapshot.documents.mapNotNull {
                it.toObject(BudgetCategory::class.java)
            }
            onResult(list)
        }
    }
}