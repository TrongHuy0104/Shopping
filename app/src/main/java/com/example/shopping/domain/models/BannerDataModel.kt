package com.example.shopping.domain.models

data class BannerDataModel(
    val name: String = "",
    val image: String = "",
    val date: Long = System.currentTimeMillis()
)
