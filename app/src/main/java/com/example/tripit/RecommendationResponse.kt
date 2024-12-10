package com.example.tripit

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    @SerializedName("recommendations")
    val recommendations: Array<RecommendedDestination>
)
