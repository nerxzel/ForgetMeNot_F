package com.example.forgetmenot.data.repository

import com.example.forgetmenot.data.local.model.user.UserEntity
import com.example.forgetmenot.data.remote.RetrofitInstance
import com.example.forgetmenot.data.remote.UserService
import com.example.forgetmenot.data.remote.dto.UserDto

class UserRepository (
    //private val userDao: UserDao
){

    private val userService = RetrofitInstance.userService
    suspend fun login(email: String, pass: String): Result<UserEntity>{
        /*val user = userDao.getByEmail(email)
        return if(user != null && user.password == pass){
            Result.success(user)
        }
        else{
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }*/
        return Result.success(UserEntity(0L, "H", "H", "h"))
    }

    suspend fun register(name:String, email: String, pass: String): Result<Long>{
        try {
            val user = userService.getUserByEmail(email)
            if (user.email == email) {
                return Result.failure(IllegalArgumentException("Correo en uso"))
            } else{
                userService.addUser(UserDto(0, name, email, pass))
                return Result.success(1)
            }
        } catch (e: Exception){
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        //return userDao.getByEmail(email)
        return UserEntity(0, "H", "H", "h")
    }

    suspend fun getUserById(id: Long): UserEntity? {
        //return userDao.getById(id)
        return UserEntity(0, "H", "H", "h")
    }

    suspend fun updateUser(user: UserEntity): Result<Unit> {
        /*return try {
            userDao.update(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }*/
        return Result.success(Unit)
    }

    suspend fun verifyPassword(userId: Long, currentPasswordToCheck: String): Result<Unit> {
        /*val user = userDao.getById(userId)
        return if (user != null && user.password == currentPasswordToCheck) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Contraseña actual incorrecta"))
        }*/
        return Result.success(Unit)
    }


    suspend fun updatePassword(userId: Long, newPassword: String): Result<Unit> {
        /*return try {
            userDao.updatePassword(userId, newPassword)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }*/
        return Result.success(Unit)
    }

}