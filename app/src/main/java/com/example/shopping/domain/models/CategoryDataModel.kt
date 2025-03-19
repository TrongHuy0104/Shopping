package com.example.shopping.domain.models

data class CategoryDataModel(
    var name: String = "",
    var date: Long = System.currentTimeMillis(),
    var createdBy: String = "",
    var categoryImage: String = ""
)
