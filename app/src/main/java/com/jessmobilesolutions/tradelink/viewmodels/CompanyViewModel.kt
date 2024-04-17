package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jessmobilesolutions.tradelink.models.Representative

class CompanyViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    val representatives: MutableList<Representative> = mutableListOf()

    fun loadRepresentatives() {
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val representedCompany = document.getString("representedCompany") ?: ""
                    val niche = document.getString("niche") ?: ""
                    val phone = document.getString("phone") ?: ""
                    val city = document.getString("city") ?: ""
                    val state = document.getString("state") ?: ""

                    val representative = Representative(name, email, representedCompany, niche, phone, city, state)
                    representatives.add(representative)
                }
            }
            .addOnFailureListener {
            }
    }
}

