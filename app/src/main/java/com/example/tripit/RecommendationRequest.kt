package com.example.tripit

data class RecommendationRequest(
    val theme: String,
    val rating: Int,
    val days: Int,
    val latitude: Double,
    val longitude: Double
)
