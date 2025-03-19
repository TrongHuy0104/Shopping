package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckoutUseCase @Inject constructor(private val repo: Repo) {
    fun getCheckout(productId: String): Flow<ResultState<ProductDataModel>> {
        return repo.getCheckout(productId)

    }
}