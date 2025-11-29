package com.example.forgetmenot.data.local.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserEntity (
    val id: Long = 0L,
    val name: String,
    val email: String,
    val password: String
)