package com.jessmobilesolutions.tradelink.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jessmobilesolutions.tradelink.models.Product

class ProductsCatalogViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    init {
        _products.value = listOf(
            Product(1, "Product 1", 10.0),
            Product(2, "Product 2", 20.0),
            Product(3, "Product 3", 30.0)
        )
    }

    fun addProduct(product: Product) {
        val productList = _products.value.orEmpty().toMutableList()
        productList.add(product)
        _products.value = productList
    }

    fun updateProduct(product: Product) {
        val productList = _products.value.orEmpty().toMutableList()
        val index = productList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            productList[index] = product
            _products.value = productList
        }
    }

    fun deleteProduct(productId: Int) {
        val productList = _products.value.orEmpty().toMutableList()
        val index = productList.indexOfFirst { it.id == productId }
        if (index != -1) {
            productList.removeAt(index)
            _products.value = productList
        }
    }
}