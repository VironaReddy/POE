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
        ref.get().addOnSuccessListener { snap ->
            val list = snap.documents.mapNotNull {
                it.toObject(IncomeCategory::class.java)
            }
            onResult(list)
        }
    }
}