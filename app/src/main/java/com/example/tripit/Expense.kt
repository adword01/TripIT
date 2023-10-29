package com.example.tripit

data class Expense(
    val personName: String = "",
    val details: List<ExpenseDetail> = emptyList()
)

