package com.example.tripit.dataclasses

data class categoryMap (
    val name: String = "",
    val quantity: String = "",
    val conditions: List<String> = emptyList()
)