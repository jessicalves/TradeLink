package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.models.Visit

class ClientRequestVisitViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createNewVisit(visit: Visit) {

        if (visit.companyID != null) {
            db.collection("users")
                .document(visit.companyID)
                .collection("visits")
                .add(visit)
                .addOnSuccessListener {
                    //ok
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
    }

}