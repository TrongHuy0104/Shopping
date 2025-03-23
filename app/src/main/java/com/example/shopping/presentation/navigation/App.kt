package com.example.shopping.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.shopping.R
import com.example.shopping.presentation.LoginScreen
import com.example.shopping.presentation.SignUpScreen
import com.example.shopping.presentation.screens.AllCategoriesScreen
import com.example.shopping.presentation.screens.AllFav
import com.example.shopping.presentation.screens.AllProducts
import com.example.shopping.presentation.screens.CartScreen
import com.example.shopping.presentation.screens.CheckoutScreen
import com.example.shopping.presentation.screens.HomeScreen
import com.example.shopping.presentation.screens.ProductDetailsScreen
import com.example.shopping.presentation.screens.CategoryItemsScreen
import com.example.shopping.presentation.screens.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

data class BottomNavItem(val name: String, val icon: ImageVector, val unselectedIcon: ImageVector)

@Composable
fun App(
    firebaseAuth: FirebaseAuth,
    startPayment:() -> Unit
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val navBackSlackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackSlackEntry?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.LoginScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
            else -> true
        }
    }

    val BottomNavItem = listOf(
        BottomNavItem(
            name = "Home",
            icon = Icons.Default.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavItem(
            name = "WishList",
            icon = Icons.Default.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavItem(
            name = "Cart",
            icon = Icons.Default.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem(
            name = "Personal",
            icon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
    )

    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(24.dp)) // Soft shadow effect
                            .background(Color.White, RoundedCornerShape(24.dp)) // White container
                    ) {
                        AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = BottomNavItem.size,
                            containerColor = Color.Transparent, // Transparent for better visual
                            indicatorColor = colorResource(id = R.color.orange),
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            indicatorStyle = IndicatorStyle.FILLED,
                        ) {
                            BottomNavItem.forEachIndexed { index, navigationItem ->
                                BottomBarItem(
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        when (index) {
                                            0 -> navController.navigate(Routes.HomeScreen)
                                            1 -> navController.navigate(Routes.WishlistScreen)
                                            2 -> navController.navigate(Routes.CartScreen)
                                            3 -> navController.navigate(Routes.ProfileScreen)
                                        }
                                    },
                                    imageVector = navigationItem.icon,
                                    label = navigationItem.name,
                                    containerColor = Color.Transparent,
                                    textColor = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (shouldShowBottomBar.value) 60.dp else 0.dp)
        ) {
            NavHost(navController = navController, startDestination = startScreen) {
                navigation<SubNavigation.LoginSignUpScreen>(
                    startDestination = Routes.LoginScreen
                ) {
                    composable<Routes.LoginScreen> {
                        LoginScreen(navController = navController)
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController = navController)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(
                    startDestination = Routes.HomeScreen
                ) {
                    composable<Routes.HomeScreen> {
                        HomeScreen(navController = navController)
                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreen(firebaseAuth, navController = navController)
                    }
                    composable<Routes.WishlistScreen> {
                        AllFav(navController = navController)
                    }
                    composable<Routes.CartScreen> {
                        CartScreen(navController = navController)
                    }
                    composable<Routes.AllProductsScreen> {
                        AllProducts(navController = navController)
                    }
                    composable<Routes.AllCategoriesScreen> {
                        AllCategoriesScreen(navController = navController)
                    }
                }
                composable<Routes.ProductDetailsScreen> {
                    val product: Routes.ProductDetailsScreen = it.toRoute()
                    ProductDetailsScreen(
                        navController = navController,
                        productId = product.productId
                    )
                }
                composable<Routes.CategoryItemsScreen> {
                    val category: Routes.CategoryItemsScreen = it.toRoute()
                    CategoryItemsScreen(
                        navController = navController,
                        productId = category.categoryName
                    )
                }
                composable<Routes.CheckoutScreen> {
                    val product: Routes.CheckoutScreen = it.toRoute()
                    val productIds: List<String> = product.productId.split(",")

                    CheckoutScreen(navController = navController, productId = productIds, pay = startPayment,  totalAmount = product.totalAmount)
                }
            }
        }
    }
}