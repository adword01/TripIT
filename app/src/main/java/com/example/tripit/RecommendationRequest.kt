package com.example.tripit

data class RecommendationRequest(
    val user_input: String,
    val city: String,
    val top_n: Int
)
