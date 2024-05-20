package com.jessmobilesolutions.tradelink.models

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    var image: String? = null
)