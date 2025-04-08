package com.example.tripit

import com.google.gson.annotations.SerializedName

data class RecommendedDestination(
//    val District: String,
  //  @SerializedName("Place Name")
    val Place_name: String,
    val city: String,
    @SerializedName("Place Name")
    val description: String,
    val processed_tags: String,
    val District: String,
    val duration: String,
    val travel_option: String,
    val travel_style: String,
    val old_age: Int,
    val young_age: Int,
    @SerializedName("Avg Expense Per Day")
    val avg_expense_per_day: Int
)
