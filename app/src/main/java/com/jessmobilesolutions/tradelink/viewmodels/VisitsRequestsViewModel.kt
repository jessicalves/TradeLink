package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.models.Visit

class VisitsRequestsViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _visits = MutableLiveData<List<Visit>>()
    val visits: LiveData<List<Visit>> = _visits

    init {
        loadVisits()
    }
    
    private fun loadVisits(){
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .collection("visits")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val visitsList = mutableListOf<Visit>()
                    for (doc in value!!) {
                        val uid = doc.getString("uid") ?: ""
                        val clientName = doc.getString("clientName") ?: ""
                        val clientPhone = doc.getString("clientPhone") ?: ""
                        val clientAddress = doc.getString("clientAddress") ?: ""
                        val companyID = doc.getString("companyID") ?: ""
                        val visit = Visit(uid,companyID, clientName, clientPhone,clientAddress)
                        visitsList.add(visit)
                    }
                    _visits.value = visitsList
                }
        }
    }

}