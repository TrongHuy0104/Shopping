package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.CartDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: Repo) {
    fun addToCart(cartDataModel: CartDataModel): Flow<ResultState<String>> {
        return repo.addToCarts(cartDataModel)

    }
}