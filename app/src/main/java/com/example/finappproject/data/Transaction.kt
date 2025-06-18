package com.example.finappproject.data

data class Transaction(
    val id: Int,
    val operation: String,
    val description: String,
    val value: Double,
    val timestamp: Long
)
