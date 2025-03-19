package com.example.shopping.data.repo

import android.net.Uri
import com.example.shopping.common.ADD_TO_CART
import com.example.shopping.common.ADD_TO_FAV
import com.example.shopping.common.BANNER_COLLECTION
import com.example.shopping.common.CATEGORY_COLLECTION
import com.example.shopping.common.PRODUCT_COLLECTION
import com.example.shopping.common.ResultState
import com.example.shopping.common.USER_COLLECTION
import com.example.shopping.domain.models.BannerDataModel
import com.example.shopping.domain.models.CartDataModel
import com.example.shopping.domain.models.CategoryDataModel
import com.example.shopping.domain.models.ProductDataModel
import com.example.shopping.domain.models.UserData
import com.example.shopping.domain.models.UserDataParent
import com.example.shopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var firebaseFirestore: FirebaseFirestore
) : Repo {
    override fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseFirestore.collection(USER_COLLECTION)
                            .document(it.result.user?.uid.toString()).set(userData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResultState.Success("User registered successfully!"))
                                } else {
                                    if (it.exception != null) {
                                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                                    }
                                }
                            }
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }

            awaitClose {
                close()
            }
        }

    override fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User logged in successfully!"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }

            awaitClose {
                close()
            }
        }

    override fun getUserById(uId: String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(USER_COLLECTION).document(uId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result.toObject(UserData::class.java)!!
                val userDataParent = UserDataParent(it.result.id, data)
                trySend(ResultState.Success(userDataParent))
            } else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose {
            close()
        }
    }

    override fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(USER_COLLECTION).document(userDataParent.nodeId)
                .update(userDataParent.userData.toMap()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User data update successfully"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }
            awaitClose {
                close()
            }
        }

    override fun userProfileImage(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        FirebaseStorage.getInstance().reference.child("userProfileImage/${System.currentTimeMillis()} + ${firebaseAuth.currentUser?.uid}")
            .putFile(uri)
            .addOnCompleteListener {
                it.result.storage.downloadUrl.addOnCompleteListener { imageUri ->
                    trySend(ResultState.Success(imageUri.toString()))
                }
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        awaitClose {
            close()
        }
    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(CATEGORY_COLLECTION).limit(7).get()
                .addOnSuccessListener { querySnapshot ->
                    val categories = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(CategoryDataModel::class.java)
                    }
                    trySend(ResultState.Success(categories))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getProductsInLimited(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(PRODUCT_COLLECTION).limit(10).get()
            .addOnSuccessListener {
                val products = it.documents.mapNotNull { document ->
                    document.toObject(ProductDataModel::class.java)?.apply {
                        productId = document.id
                    }
                }
                trySend(ResultState.Success(products))
            }.addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(PRODUCT_COLLECTION).get().addOnSuccessListener {
            val products = it.documents.mapNotNull { document ->
                document.toObject(ProductDataModel::class.java)?.apply {
                    productId = document.id
                }
            }
            trySend(ResultState.Success(products))
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId).get()
                .addOnSuccessListener {
                    val products = it.toObject(ProductDataModel::class.java)
                    trySend(ResultState.Success(products!!))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }

            awaitClose {
                close()
            }
        }

    override fun addToCarts(cartDataModel: CartDataModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid)
                .collection("User_Cart")
                .add(cartDataModel).addOnSuccessListener {
                    trySend(ResultState.Success("Added to cart successfully"))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun addToFav(productDataModel: ProductDataModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
                .collection("User_Fav")
                .add(productDataModel).addOnSuccessListener {
                    trySend(ResultState.Success("Added to fav successfully"))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getAllFav(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
            .collection("User_Fav")
            .get().addOnSuccessListener {
                val fav = it.documents.mapNotNull { document ->
                    document.toObject(ProductDataModel::class.java)
                }
                trySend(ResultState.Success(fav))
            }.addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getCart(): Flow<ResultState<List<CartDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid)
            .collection("User_Cart").get().addOnSuccessListener {
                val carts = it.documents.mapNotNull { document ->
                    document.toObject(CartDataModel::class.java)?.apply {
                        cartId = document.id
                    }
                }
                trySend(ResultState.Success(carts))
            }.addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(CATEGORY_COLLECTION).get().addOnSuccessListener {
            val categories = it.documents.mapNotNull { document ->
                document.toObject(CategoryDataModel::class.java)
            }
            trySend(ResultState.Success(categories))
        }.addOnFailureListener { exception ->
            trySend(ResultState.Error(exception.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun getCheckout(productId: String): Flow<ResultState<ProductDataModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId).get()
                .addOnSuccessListener {
                    val checkout = it.toObject(ProductDataModel::class.java)
                    trySend(ResultState.Success(checkout!!))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }
            awaitClose {
                close()
            }
        }

    override fun getBanner(): Flow<ResultState<List<BannerDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(BANNER_COLLECTION).get().addOnSuccessListener {
            val banner = it.documents.mapNotNull { document ->
                document.toObject(BannerDataModel::class.java)
            }
            trySend(ResultState.Success(banner))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getSpecificCategoryItems(categoryName: String): Flow<ResultState<List<ProductDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener { querySnapshot -> // Explicitly name it
                    val products = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(ProductDataModel::class.java)?.apply {
                            productId = document.id
                        }
                    }
                    trySend(ResultState.Success(products))
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }

            awaitClose {
                close()
            }
        }

    override fun getALlSuggestProducts(): Flow<ResultState<List<ProductDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
            .collection("User_Fav")
            .get().addOnSuccessListener {
                val fav = it.documents.mapNotNull { document ->
                    document.toObject(ProductDataModel::class.java)
                }
                trySend(ResultState.Success(fav))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }
}

