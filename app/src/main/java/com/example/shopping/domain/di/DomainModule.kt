package com.example.shopping.domain.di

import com.example.shopping.data.remote.PaymentApi
import com.example.shopping.data.repo.RepoImpl
import com.example.shopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideRepo(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        paymentApi: PaymentApi
    ): Repo {
        return RepoImpl(firebaseAuth, firebaseFirestore, paymentApi)
    }

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://prm-392-stripe-server.vercel.app/") // Use 10.0.2.2 to access localhost in emulator
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun providePaymentApi(retrofit: Retrofit): PaymentApi =
        retrofit.create(PaymentApi::class.java)
}