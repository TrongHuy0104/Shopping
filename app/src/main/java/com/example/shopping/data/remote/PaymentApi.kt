package com.example.shopping.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class PaymentRequest(val amount: Int)
data class PaymentResponse(val clientSecret: String)

interface PaymentApi {
    @Headers("Content-Type: application/json")
    @POST("/api/create-payment-intent")
    suspend fun createPaymentIntent(@Body request: PaymentRequest): Response<PaymentResponse>
}
