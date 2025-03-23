package com.example.shopping.domain.models

data class ProductDataModel(
    var name: String = "",
    var image: String = "",
    var date: Long = System.currentTimeMillis(),
    var createdBy: String = "",
    var price: String = "",
    var finalPrice: String = "",
    var description: String = "",
    var category: String = "",
    var availableUnits: Int = 0,
    var productId: String = ""
)
