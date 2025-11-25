package com.example.forgetmenot.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://forgetmenot-backend.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val articleService: ArticleService by lazy {
        retrofit.create(ArticleService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}
