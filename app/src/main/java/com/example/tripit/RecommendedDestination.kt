package com.example.tripit

import com.google.gson.annotations.SerializedName

data class RecommendedDestination(
//    val District: String,
  //  @SerializedName("Place Name")
    val Place_name: String,
    val city: String,
    val description: String,
    val Tags: String,
    val District: String,
    val Duration: String,
    val Travel_option: String,
    val Travel_style: String,
    val old_age: Int,
    val young_age: Int,
    @SerializedName("Avg Expense Per Day")
    val avg_expense_per_day: Int
)
