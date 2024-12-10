package com.example.tripit

import com.example.tripit.fragments.RecommendedDestination
import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    @SerializedName("recommendations")
    val recommendations: List<RecommendedDestination>
)
