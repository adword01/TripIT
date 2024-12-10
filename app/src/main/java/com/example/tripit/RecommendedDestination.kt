package com.example.tripit

data class RecommendedDestination(
//    val District: String,
  //  @SerializedName("Place Name")
    val place_name: String,
    val city: String,
    val description: String,
    val processed_tags: String,
    val district: String,
    val duration: String,
    val travel_option: String,
    val travel_style: String,
    val old_age: Int,
    val young_age: Int,
    val avg_expense_per_day: Int
)
