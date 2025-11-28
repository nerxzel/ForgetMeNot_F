package com.example.forgetmenot.data.remote.dto

import com.example.forgetmenot.data.local.model.Article

data class ArticleDto(
    val id: Long,
    val name: String,
    val imgUrl: String?,
    val category: String,
    val price: String,
    val condition: String,
    val acquisitionDate: String,
    val location: String,
    val idUsuario: String
)

fun ArticleDto.toArticle(): Article {
    return Article(
        id = id,
        name = name,
        description = "", // Backend doesn't seem to have description based on user comment, defaulting to empty
        imageUrl = imgUrl,
        category = category,
        price = price.toDoubleOrNull() ?: 0.0,
        condition = condition,
        purchaseDate = acquisitionDate,
        location = location,
        tags = emptyList() // Backend doesn't seem to have tags, defaulting to empty
    )
}

fun Article.toDto(idUsuario: String): ArticleDto {
    return ArticleDto(
        id = id,
        name = name,
        imgUrl = imageUrl,
        category = category,
        price = price.toString(),
        condition = condition,
        acquisitionDate = purchaseDate,
        location = location,
        idUsuario = idUsuario
    )
}
