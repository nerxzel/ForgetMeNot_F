package com.example.forgetmenot

import com.example.forgetmenot.data.local.model.user.UserEntity
import com.example.forgetmenot.data.remote.dto.ArticleDto
import com.example.forgetmenot.data.remote.dto.UserDto
import com.example.forgetmenot.data.remote.dto.toDto
import com.example.forgetmenot.data.remote.dto.toUser
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class MapperTest {

    @Test
    fun toUserDto(){
        val user = UserEntity(1, "usuario", "correo@mail.com", "password")
        val dto = user.toDto()

        assertEquals(1, dto.id)
        assertEquals("usuario", dto.name)
        assertEquals("correo@mail.com", dto.email)
        assertEquals("password", dto.password)
        assertTrue(dto is UserDto)
    }

    @Test
    fun toUser(){
        val dto = UserDto(1, "usuario", "correo@mail.com", "password")
        val user = dto.toUser()

        assertEquals(1, user.id)
        assertEquals("usuario", user.name)
        assertEquals("correo@mail.com", user.email)
        assertEquals("password", user.password)
        assertTrue(user is UserEntity)
    }
}