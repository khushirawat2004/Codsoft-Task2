package com.example.quotes

import retrofit2.Response
import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuotes(): Response<List<QuoteModel>>
}