package com.example.shopping.presentation.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.common.HomeScreenState
import com.example.shopping.common.ResultState
import com.example.shopping.domain.models.CartDataModel
import com.example.shopping.domain.models.CategoryDataModel
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.domain.models.UserData
import com.example.shopping.domain.models.UserDataParent
import com.example.shopping.domain.useCase.AddToCartUseCase
import com.example.shopping.domain.useCase.AddToFavUseCase
import com.example.shopping.domain.useCase.CreateUserUseCase
import com.example.shopping.domain.useCase.GetAllCategoriesUseCase
import com.example.shopping.domain.useCase.GetAllFavUseCase
import com.example.shopping.domain.useCase.GetAllProductsUseCase
import com.example.shopping.domain.useCase.GetAllSuggestProductsUseCase
import com.example.shopping.domain.useCase.GetBannerUseCase
import com.example.shopping.domain.useCase.GetCartUseCase
import com.example.shopping.domain.useCase.GetCheckoutUseCase
import com.example.shopping.domain.useCase.GetProductByIdUseCase
import com.example.shopping.domain.useCase.GetProductByIdsUseCase
import com.example.shopping.domain.useCase.GetProductsInLimitUseCase
import com.example.shopping.domain.useCase.GetSpecificCategoryItemsUseCase
import com.example.shopping.domain.useCase.GetUserUseCase
import com.example.shopping.domain.useCase.LoginUserUseCase
import com.example.shopping.domain.useCase.UpdateUserUseCase
import com.example.shopping.domain.useCase.UserProfileImageUseCase
import com.example.shopping.domain.useCase.getCategoryInLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val userProfileImageUseCase: UserProfileImageUseCase,
    private val getCategoryInLimitUseCase: getCategoryInLimitUseCase,
    private val getProductsInLimitUseCase: GetProductsInLimitUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductByIdsUseCase: GetProductByIdsUseCase,
    private val addToFavUseCase: AddToFavUseCase,
    private val getAllFavUseCase: GetAllFavUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val getSpecificCategoryItemsUseCase: GetSpecificCategoryItemsUseCase,
    private val getAllSuggestProductsUseCase: GetAllSuggestProductsUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getCartUseCase: GetCartUseCase
) : ViewModel() {
    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()

    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState = _updateScreenState.asStateFlow()

    private val _uploadUserProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val uploadUserProfileImageState = _uploadUserProfileImageState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _getProductByIdsState = MutableStateFlow(GetProductByIdsState())
    val getProductByIdsState = _getProductByIdsState.asStateFlow()

    private val _addToFavState = MutableStateFlow(AddToFavState())
    val addToFavState = _addToFavState.asStateFlow()

    private val _getAllFavState = MutableStateFlow(GetAllFavState())
    val getAllFavState = _getAllFavState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState = _getCheckoutState.asStateFlow()

    private val _getSpecificCategoryItemsState = MutableStateFlow(GetSpecificCategoryItemsState())
    val getSpecificCategoryItemsState = _getSpecificCategoryItemsState.asStateFlow()

    private val _getAllSuggestedProductsState = MutableStateFlow(GetAllSuggestedProductsState())
    val getAllSuggestedProductsState = _getAllSuggestedProductsState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    fun getSpecificCategoryItems(categoryName: String) {
        viewModelScope.launch {
            getSpecificCategoryItemsUseCase.getSpecificCategoryItems(categoryName)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getSpecificCategoryItemsState.value =
                                _getSpecificCategoryItemsState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getSpecificCategoryItemsState.value =
                                _getSpecificCategoryItemsState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getSpecificCategoryItemsState.value =
                                _getSpecificCategoryItemsState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getCheckoutUseCase(productId: String) {
        viewModelScope.launch {
            getCheckoutUseCase.getCheckout(productId)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getCheckoutState.value =
                                _getCheckoutState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getCheckoutState.value =
                                _getCheckoutState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getCheckoutState.value =
                                _getCheckoutState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getAllCategoriesUseCase() {
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategories()
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getAllCategoriesState.value =
                                _getAllCategoriesState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getAllCategoriesState.value =
                                _getAllCategoriesState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getAllCategoriesState.value =
                                _getAllCategoriesState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getCartUseCase() {
        viewModelScope.launch {
            getCartUseCase.getCart()
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getCartState.value =
                                _getCartState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getCartState.value =
                                _getCartState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getCartState.value =
                                _getCartState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getAllProductsUseCase() {
        viewModelScope.launch {
            getAllProductsUseCase.getAllProducts()
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getAllProductsState.value =
                                _getAllProductsState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getAllProductsState.value =
                                _getAllProductsState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getAllProductsState.value =
                                _getAllProductsState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getAllFav() {
        viewModelScope.launch {
            getAllFavUseCase.getAllFav()
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getAllFavState.value =
                                _getAllFavState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getAllFavState.value =
                                _getAllFavState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getAllFavState.value =
                                _getAllFavState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun addToFav(productDataModels: ProductDataModel) {
        viewModelScope.launch {
            addToFavUseCase.addToFav(productDataModels)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _addToFavState.value =
                                _addToFavState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _addToFavState.value =
                                _addToFavState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _addToFavState.value =
                                _addToFavState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

        fun getProductById(productId: String) {
            viewModelScope.launch {
                getProductByIdUseCase.getProductById(productId)
                    .collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                _getProductByIdState.value =
                                    _getProductByIdState.value.copy(
                                        isLoading = false,
                                        userData = result.data
                                    )
                            }

                            is ResultState.Loading -> {
                                _getProductByIdState.value =
                                    _getProductByIdState.value.copy(isLoading = true)
                            }

                            is ResultState.Error -> {
                                _getProductByIdState.value =
                                    _getProductByIdState.value.copy(
                                        isLoading = false,
                                        errorMessage = result.message
                                    )
                            }
                        }
                    }
            }
        }

    fun getProductsByIds(productIds: List<String>) {
        val productIdList = productIds.toString()
            .removeSurrounding("[[", "]]")
            .split(",")
            .map { it.trim() }
        viewModelScope.launch {
            _getProductByIdsState.value = GetProductByIdsState(isLoading = true)

            val productList = mutableListOf<ProductDataModel>()


                getProductByIdsUseCase.getProductByIds(productIdList).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getProductByIdsState.value =
                                _getProductByIdsState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getProductByIdsState.value =
                                _getProductByIdsState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getProductByIdsState.value =
                                _getProductByIdsState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }



    fun addToCart(cartDataModel: CartDataModel) {
        viewModelScope.launch {
            addToCartUseCase.addToCart(cartDataModel)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _addToCartState.value =
                                _addToCartState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _addToCartState.value =
                                _addToCartState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _addToCartState.value =
                                _addToCartState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    init {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch {
            combine(
                getCategoryInLimitUseCase.getCategoryInLimit(),
                getProductsInLimitUseCase.getProductInLimit(),
                getBannerUseCase.getBanner()
            ) { categoryResult, productResult, bannerResult ->
                when {
                    categoryResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = categoryResult.message)
                    }

                    productResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = productResult.message)
                    }

                    bannerResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = bannerResult.message)
                    }

                    categoryResult is ResultState.Success && productResult is ResultState.Success && bannerResult is ResultState.Success -> {
                        HomeScreenState(
                            isLoading = false,
                            categories = categoryResult.data,
                            products = productResult.data,
                            banners = bannerResult.data
                        )
                    }

                    else -> {
                        HomeScreenState(isLoading = true)
                    }
                }
            }.collect { state ->
                _homeScreenState.value = state
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            userProfileImageUseCase.userProfileImage(uri)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _uploadUserProfileImageState.value =
                                _uploadUserProfileImageState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _uploadUserProfileImageState.value =
                                _uploadUserProfileImageState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _uploadUserProfileImageState.value =
                                _uploadUserProfileImageState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun updateUserData(userDataParent: UserDataParent) {
        viewModelScope.launch {
            updateUserUseCase.updateUserData(userDataParent)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _updateScreenState.value =
                                _updateScreenState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _updateScreenState.value =
                                _updateScreenState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _updateScreenState.value =
                                _updateScreenState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun createUser(userData: UserData) {
        viewModelScope.launch {
            createUserUseCase.register(userData)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _signUpScreenState.value =
                                _signUpScreenState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _signUpScreenState.value =
                                _signUpScreenState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _signUpScreenState.value =
                                _signUpScreenState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun loginUser(userData: UserData) {
        viewModelScope.launch {
            loginUserUseCase.login(userData)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _loginScreenState.value =
                                _loginScreenState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _loginScreenState.value =
                                _loginScreenState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _loginScreenState.value =
                                _loginScreenState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getUserById(uid: String) {
        viewModelScope.launch {
            getUserUseCase.getUserById(uid)
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _profileScreenState.value =
                                _profileScreenState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _profileScreenState.value =
                                _profileScreenState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _profileScreenState.value =
                                _profileScreenState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }

    fun getAllSuggestedProducts() {
        viewModelScope.launch {
            getAllSuggestProductsUseCase.getAllSuggestProducts()
                .collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(
                                    isLoading = false,
                                    userData = result.data
                                )
                        }

                        is ResultState.Loading -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                        }
                    }
                }
        }
    }
}

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val userData: UserDataParent? = null,
    val errorMessage: String? = null
)

data class SignUpScreenState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class LoginScreenState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class UpdateScreenState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class UploadUserProfileImageState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class AddToCartState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val userData: ProductDataModel? = null,
    val errorMessage: String? = null
)

data class GetProductByIdsState(
    val isLoading: Boolean = false,
    val userData: List<ProductDataModel>? = null,
    val errorMessage: String? = null
)

data class AddToFavState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null
)

data class GetAllFavState(
    val isLoading: Boolean = false,
    val userData: List<ProductDataModel?> = emptyList(),
    val errorMessage: String? = null
)

data class GetAllProductsState(
    val isLoading: Boolean = false,
    val userData: List<ProductDataModel?> = emptyList(),
    val errorMessage: String? = null
)

data class GetCartState(
    val isLoading: Boolean = false,
    val userData: List<CartDataModel?> = emptyList(),
    val errorMessage: String? = null
)

data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val userData: List<CategoryDataModel?> = emptyList(),
    val errorMessage: String? = null
)

data class GetCheckoutState(
    val isLoading: Boolean = false,
    val userData: ProductDataModel? = null,
    val errorMessage: String? = null
)

data class GetSpecificCategoryItemsState(
    val isLoading: Boolean = false,
    val userData: List<ProductDataModel?> = emptyList(),
    val errorMessage: String? = null
)

data class GetAllSuggestedProductsState(
    val isLoading: Boolean = false,
    val userData: List<ProductDataModel?> = emptyList(),
    val errorMessage: String? = null
)