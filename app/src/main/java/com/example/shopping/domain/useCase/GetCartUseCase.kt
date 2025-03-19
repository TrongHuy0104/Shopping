package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.CartDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repo: Repo) {
    fun getCart(): Flow<ResultState<List<CartDataModel>>> {
        return repo.getCart()

    }
}