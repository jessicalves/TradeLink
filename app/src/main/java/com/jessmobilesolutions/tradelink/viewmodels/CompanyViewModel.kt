package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jessmobilesolutions.tradelink.models.Representative

class CompanyViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives
    private lateinit var originalRepresentatives: List<Representative> 

    fun loadRepresentatives() {
        db.collection("users")
            .whereEqualTo("userType", "representative")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val representativeList = mutableListOf<Representative>()
                for (document in querySnapshot) {
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val representedCompany = document.getString("representedCompany") ?: ""
                    val niche = document.getString("niche") ?: ""
                    val phone = document.getString("phone") ?: ""
                    val city = document.getString("city") ?: ""
                    val state = document.getString("state") ?: ""

                    val representative = Representative(name, email, representedCompany, niche, phone, city, state)
                    representativeList.add(representative)
                }
                _representatives.value = representativeList
                originalRepresentatives = representativeList 
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun filterRepresentatives(query: String) {
        val currentList = if (::originalRepresentatives.isInitialized) originalRepresentatives else emptyList()
        val filteredList = if (query.isEmpty()) {
            currentList
        } else {
            currentList.filter { representative ->
                representative.representedCompany.contains(query, ignoreCase = true) ||
                        representative.niche.contains(query, ignoreCase = true)
            }
        }
        _representatives.value = filteredList
    }
}

