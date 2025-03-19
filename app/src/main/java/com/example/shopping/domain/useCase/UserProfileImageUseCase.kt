package com.example.shopping.domain.useCase

import android.net.Uri
import com.example.shopping.common.ResultState
import com.example.shopping.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileImageUseCase @Inject constructor(private val repo: Repo) {
    fun userProfileImage(uri: Uri): Flow<ResultState<String>> {
        return repo.userProfileImage(uri)

    }
}

