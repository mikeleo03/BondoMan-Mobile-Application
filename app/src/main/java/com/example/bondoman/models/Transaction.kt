package com.example.bondoman.models

import java.util.*

data class Transaction(
    val id: Long,
    val title: String,
    val category: String,
    val amount: Int,
    val location: String,
    val date: Date
)