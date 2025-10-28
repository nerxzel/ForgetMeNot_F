package com.example.forgetmenot.data.repository

import com.example.forgetmenot.data.local.model.user.UserDao
import com.example.forgetmenot.data.local.model.user.UserEntity

class UserRepository (
    private val userDao: UserDao
){
    suspend fun login(email: String, pass: String): Result<UserEntity>{
        val user = userDao.getByEmail(email)
        return if(user != null && user.password == pass){
            Result.success(user)
        }
        else{
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    suspend fun register(name:String, email: String, pass: String): Result<Long>{
        val exists = userDao.getByEmail(email) != null
        if(exists){
            return Result.failure(IllegalArgumentException("Correo en uso"))
        }
        else{
            val id = userDao.insert(
                UserEntity(
                    name = name,
                    email = email,
                    password = pass
                )
            )
            return Result.success(id)
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getByEmail(email)
    }

    suspend fun getUserById(id: Long): UserEntity? {
        return userDao.getById(id)
    }

    suspend fun updateUser(user: UserEntity): Result<Unit> {
        return try {
            userDao.update(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyPassword(userId: Long, currentPasswordToCheck: String): Result<Unit> {
        val user = userDao.getById(userId)
        return if (user != null && user.password == currentPasswordToCheck) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Contraseña actual incorrecta"))
        }
    }


    suspend fun updatePassword(userId: Long, newPassword: String): Result<Unit> {
        return try {
            userDao.updatePassword(userId, newPassword)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}