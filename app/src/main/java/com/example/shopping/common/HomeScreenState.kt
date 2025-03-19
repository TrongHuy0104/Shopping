package com.example.shopping.common

import com.example.shopping.domain.models.BannerDataModel
import com.example.shopping.domain.models.CategoryDataModel
import com.example.shopping.domain.models.ProductDataModel

data class HomeScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String?= null,
    val categories: List<CategoryDataModel>?= null,
    val products: List<ProductDataModel>?= null,
    val banners: List<BannerDataModel>?= null
)
