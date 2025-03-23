package com.example.shopping.domain.repo

import android.net.Uri
import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.BannerDataModel
import com.example.shopping.domain.models.CartDataModel
import com.example.shopping.domain.models.CategoryDataModel
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.domain.models.UserData
import com.example.shopping.domain.models.UserDataParent
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun getUserById(uId: String): Flow<ResultState<UserDataParent>>
    fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>>
    fun userProfileImage(uri: Uri): Flow<ResultState<String>>
    fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>>
    fun getProductsInLimited(): Flow<ResultState<List<ProductDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>>
    fun getProductById(productId: String): Flow<ResultState<ProductDataModel>>
    fun getProductByIds(productId: List<String>): Flow<ResultState<List<ProductDataModel>>>

    fun addToCarts(cartDataModel: CartDataModel): Flow<ResultState<String>>
    fun addToFav(productDataModel: ProductDataModel): Flow<ResultState<String>>
    fun getAllFav(): Flow<ResultState<List<ProductDataModel>>>
    fun getCart(): Flow<ResultState<List<CartDataModel>>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>>
    fun getCheckout(productId: String): Flow<ResultState<ProductDataModel>>
    fun getBanner(): Flow<ResultState<List<BannerDataModel>>>
    fun getSpecificCategoryItems(categoryName: String): Flow<ResultState<List<ProductDataModel>>>
    fun getALlSuggestProducts(): Flow<ResultState<List<ProductDataModel>>>

}