package com.example.tripit.dataclasses

data class Category(
    val categoryName: String,
    val items: List<categoryMap> = emptyList()
)
