package com.example.forgetmenot.data.remote

import com.example.forgetmenot.data.remote.dto.ArticleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {

    @GET("article/all/mail")
    suspend fun getArticles(@Query("email") email: String): List<ArticleDto>

    @GET("article/{id}")
    suspend fun getArticleById(@Path("id") id: Long): ArticleDto

    @POST("article")
    suspend fun addArticle(@Body article: ArticleDto): ArticleDto

    @PUT("article/{id}")
    suspend fun updateArticle(@Path("id") id: Long, @Body article: ArticleDto): ArticleDto

    @DELETE("article/{id}")
    suspend fun deleteArticle(@Path("id") id: Long)
}
