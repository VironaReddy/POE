package com.example.practice.data

import com.example.practice.model.IncomeCategory
import com.google.firebase.firestore.FirebaseFirestore

class IncomeCategoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("incomeCategories")

    fun addCategory(category: IncomeCategory) {
        ref.add(category)
    }

    fun getCategories(onResult: (List<IncomeCategory>) -> Unit) {
        ref.addSnapshotListener { snapshot, error ->

            if (error != null || snapshot == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(IncomeCategory::class.java)
            }

            onResult(list)
        }
    }
}