package com.example.tripit.fragments

import com.google.gson.annotations.SerializedName

data class RecommendedDestination(
    val District: String,
    @SerializedName("Place Name")
    val placeName: String,
    val Theme: String,
    val Rating: Double
)
