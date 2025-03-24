package com.example.shopping.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shopping.R
import com.example.shopping.presentation.navigation.Routes
import com.example.shopping.presentation.viewModels.ShoppingAppViewModel
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSuccessScreen(
    navController: NavController,
    orderId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val orderState by viewModel.orderDetailState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.getOrderDetails(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (orderState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (orderState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = orderState.errorMessage!!, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            orderState.orderData?.let { order ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.online_shopping),
                            contentDescription = "Order Success",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Thank you for your order!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium,
                            color = colorResource(id = R.color.orange))}
                    Spacer(modifier = Modifier.height(30.dp))

                    Text("User Information", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Name: ")
                                    }
                                    append("${order.firstName} ${order.lastName}")
                                }
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Email: ")
                                    }
                                    append(order.email)
                                }
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Address: ")
                                    }
                                    append("${order.address}, ${order.city}, ${order.country}, ${order.postalCode}")
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product List
                    Text("Products", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    order.products.forEach { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.image,
                                    contentDescription = "${product.name} image",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp)
                                ){
                                    Text("${product.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                    Text("$${product.price}", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Shipping Method
                    Text("Shipping Method", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(order.selectedMethod, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Amount
                    val totalAmount = order.products.sumOf { it.price.toInt() }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.orange), fontWeight = FontWeight.Bold)) {
                                append("Total Amount: ")
                            }
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("$$totalAmount")
                            }
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { navController.navigate(Routes.HomeScreen) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
                    ) {
                        Text("Return to Homepage", color = Color.White)
                    }
                }
            }
        }
    }
}