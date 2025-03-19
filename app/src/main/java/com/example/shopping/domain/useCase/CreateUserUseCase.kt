package com.example.shopping.domain.useCase

import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.UserData
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: Repo) {
    fun register(userData: UserData): Flow<ResultState<String>> {
        return repo.registerUserWithEmailAndPassword(userData)

    }
}

