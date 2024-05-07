package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.models.Product

class NewSalesViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun loadProducts() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .collection("products")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val productList = mutableListOf<Product>()
                    for (doc in value!!) {
                        val id = doc.getString("id") ?: ""
                        val name = doc.getString("name") ?: ""
                        val price = doc.getDouble("price") ?: 0.0
                        val product = Product(id, name, price)
                        productList.add(product)
                    }
                    _products.value = productList
                }
        }
    }

    fun saveSale(
        clientName: String,
        saleDate: String,
        total: String,
        soldProducts: List<Pair<Product, Int>>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val saleData = hashMapOf(
                "clientName" to clientName,
                "saleDate" to saleDate,
                "total" to total,
                "soldProducts" to soldProducts
            )

            db.collection("users")
                .document(currentUser.uid)
                .collection("sales")
                .add(saleData)
                .addOnSuccessListener { documentReference ->
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e.message ?: "Erro ao salvar a venda")
                }
        }
    }
}