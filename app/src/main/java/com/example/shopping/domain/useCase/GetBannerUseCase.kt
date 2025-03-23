package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.BannerDataModel
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannerUseCase @Inject constructor(private val repo: Repo) {
    fun getBanner(): Flow<ResultState<List<BannerDataModel>>> {
        return repo.getBanner()

    }
}