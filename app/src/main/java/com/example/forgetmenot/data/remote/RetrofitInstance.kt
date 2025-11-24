package com.example.forgetmenot.data.remote

import retrofit2.Retrofit
import retrofit2.converter-gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://forgetmenot-backend.onrender.com/"

    val api: ArticleService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArticleService::class.java)
    }
}
