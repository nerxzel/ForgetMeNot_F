package com.example.forgetmenot.data.local.model

data class Article(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val category: String,
    val price: Double,
    val condition: String,
    val purchaseDate: String,
    val location: String,
    val tags: List<String>
)