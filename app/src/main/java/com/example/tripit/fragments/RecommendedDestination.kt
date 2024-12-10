package com.example.tripit.fragments

import com.google.gson.annotations.SerializedName

data class RecommendedDestination(
//    val District: String,
  //  @SerializedName("Place Name")
    val place_name: String,
    val city: String,
    val description: String
)
