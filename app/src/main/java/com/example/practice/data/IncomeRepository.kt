package com.example.practice.data

import android.util.Log
import com.example.practice.model.Income
import com.google.firebase.firestore.FirebaseFirestore

class IncomeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("incomes")

    fun addIncome(income: Income) {
        ref.add(income)
            .addOnSuccessListener {
                Log.d("IncomeRepo", "Income saved successfully")
            }
            .addOnFailureListener {
                Log.e("IncomeRepo", "Failed to save income: ${it.message}")
            }
    }

    fun getIncomes(onResult: (List<Income>) -> Unit) {
        ref.get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull {
                    it.toObject(Income::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                Log.e("IncomeRepo", "Failed to load incomes: ${it.message}")
                onResult(emptyList())
            }
    }
}