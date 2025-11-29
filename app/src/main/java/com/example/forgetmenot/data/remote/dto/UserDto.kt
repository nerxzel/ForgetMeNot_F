package com.example.forgetmenot.data.remote.dto

import com.example.forgetmenot.data.local.model.user.UserEntity

data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    var password: String
)

fun UserDto.toUser(): UserEntity{
    return UserEntity(
        id = id,
        name = name,
        email = email,
        password = password
    )
}

fun UserEntity.toDto(): UserDto{
    return UserDto (
        id = id,
        name = name,
        email = email,
        password = password
    )
}