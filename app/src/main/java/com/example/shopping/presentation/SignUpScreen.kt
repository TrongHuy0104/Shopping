package com.example.shopping.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.shopping.R
import com.example.shopping.domain.models.UserData
import com.example.shopping.presentation.navigation.Routes
import com.example.shopping.presentation.navigation.SubNavigation
import com.example.shopping.presentation.utils.CustomTextField
import com.example.shopping.presentation.utils.SuccessDialog
import com.example.shopping.presentation.viewModels.ShoppingAppViewModel

@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.signUpScreenState.collectAsStateWithLifecycle()

    if (state.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (state.value.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessage!!)
        }
    } else if (state.value.userData != null) {
        SuccessDialog(onClick = {
            navController.navigate(SubNavigation.MainHomeScreen)
        })
    } else {
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            // First Name
            CustomTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Last Name
            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Email
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Phone Number
            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone",
                leadingIcon = Icons.Default.Phone,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            // Password
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            // Confirm Password
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (
                        firstName.isNotEmpty()
                        && lastName.isNotEmpty()
                        && email.isNotEmpty()
                        && phoneNumber.isNotEmpty()
                        && password.isNotEmpty()
                        && confirmPassword.isNotEmpty()
                    ) {
                        if (password == confirmPassword) {
                            val userData = UserData(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                phoneNumber = phoneNumber
                            )
                            viewModel.createUser(
                                userData = userData,
                            )
                            Toast.makeText(context, "Sign Up Successfully!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Confirm Password is not correct!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all details!", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange)),
            ) {
                Text(
                    "Sign Up",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(2.dp),
                    color = colorResource(id = R.color.white)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?")
                TextButton(onClick = {
                    navController.navigate(Routes.LoginScreen)
                }) {
                    Text("Login", color = colorResource(id = R.color.orange))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(text = "Or", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 14.sp)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Login with Google", fontSize = 16.sp)
            }
        }
    }

}