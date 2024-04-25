package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jessmobilesolutions.tradelink.models.Product

class CompanyProductsViewModel :  ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun getProductsForCompany(companyId: String): LiveData<List<Product>> {
        db.collection("users")
            .document(companyId) 
            .collection("products")
            .addSnapshotListener { value, error ->
                if (error != null) {
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
        return products
    }
}