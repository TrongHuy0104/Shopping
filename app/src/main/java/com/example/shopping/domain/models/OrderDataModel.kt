package com.example.shopping.domain.models

data class OrderDataModel(
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val postalCode: String = "",
    val products: List<ProductOrder> = emptyList(),
    val selectedMethod: String = "",
    val timestamp: Long = 0L
)

data class ProductOrder(
    val name: String = "",
    val price: String = "",
    val image: String = "",
    var quantity: Int = 1
)
