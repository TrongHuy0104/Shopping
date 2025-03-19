package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsInLimitUseCase @Inject constructor(private val repo: Repo) {
    fun getProductInLimit(): Flow<ResultState<List<ProductDataModel>>> {
        return repo.getProductsInLimited()

    }
}
