package com.example.forgetmenot.data.remote

import com.example.forgetmenot.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.Query

interface UserService {

    @GET("user/email")
    suspend fun getUserByEmail(@Query("email") email: String): UserDto

    @POST("user")
    suspend fun addUser(@Body user: UserDto): UserDto

    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto): UserDto

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: Long)
}