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

    private const val BASE_URL = "https://9216-34-83-239-152.ngrok-free.app/"

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
        @GET("recommend")

        fun postData(@Query("theme") theme: String,
                     @Query("rating") rating: Int,
                     @Query("days") days: Int,
                     @Query("latitude") latitude: Double,
                     @Query("longitude") longitude: Double): Call<RecommendationResponse>
       // fun getRecommendations(@Body request: RecommendationRequest): Call<RecommendationResponse>
    }
}