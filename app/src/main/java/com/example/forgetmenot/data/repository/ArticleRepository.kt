package com.example.forgetmenot.data.repository

import com.example.forgetmenot.data.local.model.Article
import com.example.forgetmenot.data.remote.RetrofitInstance
import com.example.forgetmenot.data.remote.dto.toArticle
import com.example.forgetmenot.data.remote.dto.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository (

) {

    private val articleService = RetrofitInstance.articleService

    suspend fun getAllArticles(email: String): List<Article> = withContext(Dispatchers.IO) {
        try {
            articleService.getArticles(email).map { it.toArticle() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addArticle(article: Article, email: String) = withContext(Dispatchers.IO) {
        try {
            articleService.addArticle(article.toDto(email))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateArticle(article: Article, email: String) = withContext(Dispatchers.IO) {
        try {
            articleService.updateArticle(article.id, article.toDto(email))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteArticle(articleId: Long) = withContext(Dispatchers.IO) {
        try {
            articleService.deleteArticle(articleId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}