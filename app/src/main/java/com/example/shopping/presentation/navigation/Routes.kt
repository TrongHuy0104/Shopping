package com.example.shopping.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation {
    @Serializable
    data object LoginSignUpScreen : SubNavigation()

    @Serializable
    data object MainHomeScreen : SubNavigation()
}

sealed class Routes {
    @Serializable
    object LoginScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishlistScreen

    @Serializable
    object CartScreen

    @Serializable
    data class CheckoutScreen(val productId: String)

    @Serializable
    object PaymentScreen

    @Serializable
    object AllProductsScreen

    @Serializable
    data class ProductDetailsScreen(val productId: String)

    @Serializable
    object AllCategoriesScreen

    @Serializable
    data class CategoryItemsScreen(val categoryName: String)

}