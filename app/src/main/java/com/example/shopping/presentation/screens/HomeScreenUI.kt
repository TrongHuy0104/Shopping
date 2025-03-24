package com.example.shopping.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopping.R
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.presentation.navigation.Routes
import com.example.shopping.presentation.utils.Banner
import com.example.shopping.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    val getAllSuggestedProduct =
        viewModel.getAllSuggestedProductsState.collectAsStateWithLifecycle()
    val getSuggestedProductData: List<ProductDataModel> =
        getAllSuggestedProduct.value.userData.orEmpty().filterNotNull()
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Điều khiển dropdown mở/đóng
    val searchState by viewModel.searchProductsState.collectAsStateWithLifecycle()
    val searchResults by remember(searchState.userData) {
        mutableStateOf(searchState.userData.orEmpty())
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {
        viewModel.getAllSuggestedProducts()
        expanded = isFocused && searchQuery.isNotEmpty() && searchResults.isNotEmpty()
        Log.d("Search", "UI received products: $searchResults")
    }

    if (homeState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (homeState.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = homeState.errorMessage!!)
        }
    } else {
        Scaffold() { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { query ->
                                    searchQuery = query
                                    if (query.isNotEmpty()) {
                                        viewModel.searchProducts(query)
                                    } else {
                                        viewModel.clearSearchResults()
                                    }
                                    expanded = isFocused && searchResults.isNotEmpty()
                                },
                                placeholder = { Text("Search...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isFocused = focusState.isFocused
                                        expanded = focusState.isFocused && searchQuery.isNotEmpty() && searchResults.isNotEmpty()
                                    },
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { keyboardController?.hide() }
                                )
                            )
                            IconButton(
                                onClick = { /* TODO: Xử lý khi nhấn nút thông báo */ },
                                modifier = Modifier.padding(start = 8.dp) // 🔹 Thêm khoảng cách giữa search và icon
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Thông báo",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expanded && searchResults.isNotEmpty(),
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(335.dp)
                                .background(Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2) // 🔹 Giới hạn 1/2 màn hình
                                    .verticalScroll(rememberScrollState()) // ✅ Thay LazyColumn bằng Scrollable Column
                            ) {
                                Column {
                                    searchResults.forEach { product ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                keyboardController?.hide()
                                                navController.navigate(Routes.ProductDetailsScreen(product.productId))
                                            },
                                            text = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    AsyncImage(
                                                        model = product.image,
                                                        contentDescription = product.name,
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(CircleShape),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(product.name)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

//                Category Section
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Categories", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "See More", color = colorResource(id = R.color.orange),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.AllCategoriesScreen)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(homeState.categories ?: emptyList()) { category ->
                            CategoryItem(
                                ImageUri = category.categoryImage,
                                Category = category.name,
                                onClick = {
                                    navController.navigate(Routes.CategoryItemsScreen(categoryName = category.name))
                                }
                            )
                        }
                    }
                }

                homeState.banners?.let { banners ->
                    Banner(banners = banners)
                }

//                Flash Sale Section
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Flash Sale", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "See More, ",
                            color = colorResource(id = R.color.orange),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.AllProductsScreen)
                            }
                        )
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(homeState.products ?: emptyList()) {
                            product -> ProductCard(
                                product = product,
                                navController = navController
                            )
                        }
                    }
                }

//                Suggested
                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 5.dp)
                ) {
                    when {
                        getAllSuggestedProduct.value.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        getAllSuggestedProduct.value.errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(getAllSuggestedProduct.value.errorMessage!!)
                            }
                        }
                        getSuggestedProductData.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No product to suggest like one")
                            }
                        }
                        else -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Suggested For You", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "See More, ",
                                    color = colorResource(id = R.color.orange),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.clickable {
                                        navController.navigate(Routes.AllProductsScreen)
                                    }
                                )
                            }

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(getSuggestedProductData) {
                                        product -> ProductCard(
                                    product = product,
                                    navController = navController
                                )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    ImageUri: String,
    Category: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            AsyncImage(
                model = ImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Text(Category, style = MaterialTheme.typography.bodyMedium)
    }
}

//Flash sell section
@Composable
fun ProductCard(
    product: ProductDataModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate((Routes.ProductDetailsScreen(productId = product.productId)))
            }
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1f),
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "$${product.finalPrice}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "$${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "$${product.availableUnits} left",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
