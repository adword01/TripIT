package com.example.tripit

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

object PostApiPlaces {

    private const val BASE_URL = "https://3ecd-34-148-155-154.ngrok-free.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    interface ApiService {
        @POST("get-recommendations/")
        fun getRecommendations(@Body request: RecommendationRequest): Call<List<RecommendedDestination>>

        // fun getRecommendations(@Body request: RecommendationRequest): Call<RecommendationResponse>
    }
}