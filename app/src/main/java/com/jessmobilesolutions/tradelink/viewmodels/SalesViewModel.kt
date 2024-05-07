package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.models.Sale

class SalesViewModel : ViewModel() {
    private val _sales = MutableLiveData<List<Sale>>()
    val sales: LiveData<List<Sale>> = _sales

    fun fetchSalesFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .collection("sales")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val sales = mutableListOf<Sale>()
                    for (document in value!!) {
                        val clientName = document.getString("clientName") ?: ""
                        val saleDate = document.getString("saleDate") ?: ""
                        val total = document.getString("total") ?: ""
                        sales.add(Sale(clientName, saleDate, total))
                    }
                    _sales.value = sales

                }
        }
    }
}
