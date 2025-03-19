package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.CategoryDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repo: Repo) {
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> {
        return repo.getAllCategories()

    }
}