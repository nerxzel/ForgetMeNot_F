package com.example.forgetmenot.data.repository

import android.database.Cursor
import com.example.forgetmenot.data.local.model.Article
import com.example.forgetmenot.data.local.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository(private val dbHelper: DatabaseHelper) {

    suspend fun getAllArticles(): List<Article> = withContext(Dispatchers.IO) {
        dbHelper.readAllData()?.use { cursor ->
            parseCursorToArticleList(cursor)
        } ?: emptyList()
    }

    suspend fun addArticle(article: Article) = withContext(Dispatchers.IO) {
        dbHelper.addItem(
            name = article.name,
            description = article.description,
            category = article.category,
            date = article.purchaseDate,
            location = article.location,
            condition = article.condition,
            price = article.price,
            imageUrl = article.imageUrl,
            tags = article.tags.joinToString(",")
        )

    }

    suspend fun updateArticle(article: Article) = withContext(Dispatchers.IO) {
        dbHelper.updateData(
            rowId = article.id.toString(),
            name = article.name,
            description = article.description,
            category = article.category,
            date = article.purchaseDate,
            location = article.location,
            condition = article.condition,
            price = article.price,
            imageUrl = article.imageUrl,
            tags = article.tags.joinToString(",")
        )
    }

    suspend fun deleteArticle(articleId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteData(articleId.toString())

    }


    private fun parseCursorToArticleList(cursor: Cursor): List<Article> {
        val articles = mutableListOf<Article>()

        val idCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)
        val nameCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)
        val descCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)
        val catCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)
        val dateCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_ACQUISITION)
        val locCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION)
        val condCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONDITION)
        val priceCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)
        val imgCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL)
        val tagsCol = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TAGS)

        while (cursor.moveToNext()) {
            val tagsString = cursor.getString(tagsCol) ?: ""
            articles.add(
                Article(
                    id = cursor.getLong(idCol),
                    name = cursor.getString(nameCol) ?: "",
                    description = cursor.getString(descCol) ?: "",
                    category = cursor.getString(catCol) ?: "",
                    purchaseDate = cursor.getString(dateCol) ?: "",
                    location = cursor.getString(locCol) ?: "",
                    condition = cursor.getString(condCol) ?: "",
                    price = cursor.getDouble(priceCol),
                    imageUrl = cursor.getString(imgCol),
                    tags = tagsString.split(',')
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                )
            )
        }
        return articles
    }
}