package com.example.shopping.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopping.R
import com.example.shopping.presentation.viewModels.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    productId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    pay: () -> Unit
) {
    val state = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val productData = state.value.userData

    val email = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val selectedMethod = remember { mutableStateOf("Standard FREE delivery over Rs. 4500") }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Shipping") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            state.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.value.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Sorry, unable to get information!")
                }
            }

            productData == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "No product available!")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = state.value.userData!!.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .border(1.dp, Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = state.value.userData!!.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$${state.value.userData!!.finalPrice}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text("Contact Information", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text("Shipping Address", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = country.value,
                            onValueChange = { email.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Country/Region") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = firstName.value,
                                onValueChange = { firstName.value = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                label = { Text("First Name") }
                            )
                            OutlinedTextField(
                                value = lastName.value,
                                onValueChange = { lastName.value = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Last Name") }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address.value,
                            onValueChange = { address.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Address") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            OutlinedTextField(
                                value = city.value,
                                onValueChange = { city.value = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                label = { Text("City") }
                            )
                            OutlinedTextField(
                                value = postalCode.value,
                                onValueChange = { postalCode.value = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Postal Code") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Column {
                        Text("Shipping Method", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedMethod.value == "Standard FREE delivery over Rs. 4500",
                                onClick = {
                                    selectedMethod.value = "Standard FREE delivery over 4500"
                                })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Standard FREE delivery over 4500")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedMethod.value == "Cash on delivery Rs.50",
                                onClick = {
                                    selectedMethod.value = "Cash on delivery Rs.50"
                                })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cash on delivery Rs.50")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            pay.invoke()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
                    ) {
                        Text("Continue to Shipping")
                    }
                }
            }
        }
    }
}