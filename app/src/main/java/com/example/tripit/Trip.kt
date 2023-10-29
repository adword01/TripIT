package com.example.tripit

data class Trip(
    val tripName: String = "",
    val expenses: List<Expense> = emptyList()
)
