package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.UserDataParent
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: Repo) {
    fun getUserById(uId: String): Flow<ResultState<UserDataParent>> {
        return repo.getUserById(uId)

    }
}

