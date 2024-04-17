package com.jessmobilesolutions.tradelink.viewmodels
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewClientViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createNewUser(
        email: String,
        password: String,
        name: String,
        city: String,
        state: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = hashMapOf(
                        "uid" to uid,
                        "email" to email,
                        "name" to name,
                        "phone" to phone,
                        "city" to city,
                        "state" to state,
                        "userType" to "client"
                    )
                    uid?.let {
                        firestore.collection("users")
                            .document(it)
                            .set(user)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e -> onFailure(e.message ?: "Unknown error") }
                    }
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }
}
