package com.example.shopping.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.shopping.R
import com.example.shopping.domain.models.UserData
import com.example.shopping.domain.models.UserDataParent
import com.example.shopping.presentation.navigation.SubNavigation
import com.example.shopping.presentation.utils.LogoutDialog
import com.example.shopping.presentation.viewModels.ShoppingAppViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    firebaseAuth: FirebaseAuth,
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getUserById(firebaseAuth.currentUser!!.uid)
    }

    val profileScreenState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val updateScreenState = viewModel.updateScreenState.collectAsStateWithLifecycle()
    val userProfileImageState = viewModel.uploadUserProfileImageState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }

    val isEditing = remember { mutableStateOf(false) }
//    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imageUri = remember { mutableStateOf<String?>("") }

    val firstName =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.firstName ?: "") }
    val lastName =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.lastName ?: "") }
    val email =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.email ?: "") }
    val phoneNumber =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.phoneNumber ?: "") }
    val address =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.address ?: "") }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.userData?.let { userData ->
            firstName.value = userData.firstName ?: ""
            lastName.value = userData.lastName ?: ""
            email.value = userData.email ?: ""
            phoneNumber.value = userData.phoneNumber ?: ""
            address.value = userData.address ?: ""
            imageUri.value = (userData.profileImage ?: "")
        }
    }
    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                viewModel.uploadProfileImage(uri)
                imageUri.value = uri.toString()
            }
        }

    if (updateScreenState.value.userData != null) {
        Toast.makeText(context, updateScreenState.value.userData, Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.errorMessage != null) {
        Toast.makeText(context, updateScreenState.value.errorMessage, Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (userProfileImageState.value.userData != null) {
        imageUri.value = userProfileImageState.value.userData.toString()
    } else if (userProfileImageState.value.errorMessage != null) {
        Toast.makeText(context, userProfileImageState.value.errorMessage, Toast.LENGTH_SHORT).show()
    } else if (userProfileImageState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (profileScreenState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (profileScreenState.value.errorMessage != null) {
        Text(text = profileScreenState.value.errorMessage!!)
    } else if (profileScreenState.value.userData != null) {
        Scaffold(
// Add your Scaffold content here
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Start)
                ) {
                    SubcomposeAsyncImage(
                        model = if (isEditing.value) imageUri.value else imageUri.value,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, color = colorResource(id = R.color.orange), CircleShape)
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> {
                                CircularProgressIndicator()
                            }

                            is AsyncImagePainter.State.Error -> Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile Picture"
                            )

                            else -> SubcomposeAsyncImageContent()
                        }
                    }

                    if (isEditing.value) {
                        IconButton(
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Change Picture",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))
                Row {
                    OutlinedTextField(
                        value = firstName.value,
                        onValueChange = { firstName.value = it },

                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.orange),
                            focusedBorderColor = colorResource(id = R.color.orange)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "First Name") }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedTextField(
                        value = lastName.value,
                        onValueChange = { lastName.value = it },

                        modifier = Modifier.weight(1f),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.orange),
                            focusedBorderColor = colorResource(id = R.color.orange)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "Last Name") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },

                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        readOnly = if (isEditing.value) false else true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(id = R.color.orange),
                            focusedBorderColor = colorResource(id = R.color.orange)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "Email") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },

                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.orange),
                        focusedBorderColor = colorResource(id = R.color.orange)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Phone Number") }
                )

                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {

                OutlinedTextField(
                    value = address.value,
                    onValueChange = { address.value = it },

                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    readOnly = if (isEditing.value) false else true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.orange),
                        focusedBorderColor = colorResource(id = R.color.orange)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Address") }
                )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {

                    OutlinedButton(
                        onClick = { showDialog.value = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
                    ) {
                        Text("Log out")
                    }
                }

                if (showDialog.value) {
                    LogoutDialog(
                        onDismiss = {
                            showDialog.value = false
                        },
                        onConfirm = {
                            firebaseAuth.signOut()
                            navController.navigate(SubNavigation.LoginSignUpScreen)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                if (isEditing.value == false) {
                    OutlinedButton(
                        onClick = {
                            isEditing.value = !isEditing.value
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Edit Profile")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            val updatedUserData = UserData(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                address = address.value,
                                profileImage = imageUri.value ?: ""
                            )
                            val userDataParent = UserDataParent(
                                nodeId = profileScreenState.value.userData!!.nodeId,
                                userData = updatedUserData
                            )
                            viewModel.updateUserData(userDataParent)
                            isEditing.value = !isEditing.value
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save profile")
                    }
                }
            }
        }
    }
}