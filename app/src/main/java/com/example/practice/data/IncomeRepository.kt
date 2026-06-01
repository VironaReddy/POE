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
                Log.d("IncomeRepo", "Income saved")
            }
            .addOnFailureListener {
                Log.e("IncomeRepo", "Error: ${it.message}")
            }
    }

    fun getIncomes(onResult: (List<Income>) -> Unit) {
        ref.addSnapshotListener { snapshot, error ->

            if (error != null || snapshot == null) {
                onResult(emptyList())
                return@addSnapshotListener
            }

            val list = snapshot.documents.mapNotNull {
                it.toObject(Income::class.java)
            }

            onResult(list)
        }
    }
}