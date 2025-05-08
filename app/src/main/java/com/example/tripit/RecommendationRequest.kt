package com.example.tripit

data class RecommendationRequest(
    val tags: List<String>,
    val district: String,
    val count: Int
)
