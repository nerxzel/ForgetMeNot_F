package com.example.forgetmenot.data.repository

import com.example.forgetmenot.data.local.model.user.UserEntity
import com.example.forgetmenot.data.remote.RetrofitInstance
import com.example.forgetmenot.data.remote.dto.UserDto
import com.example.forgetmenot.data.remote.dto.toDto
import com.example.forgetmenot.data.remote.dto.toUser
import retrofit2.HttpException

class UserRepository (
    //private val userDao: UserDao
){

    private val userService = RetrofitInstance.userService
    suspend fun login(email: String, pass: String): Result<UserEntity>{
        try {
            val user = userService.getUserByEmail(email)
            if(user.password == pass){
                return Result.success(user.toUser())
            }
            return Result.failure(IllegalArgumentException("Credenciales incorrectas"))
        } catch (e: Exception){
            return Result.failure(IllegalArgumentException("Usuario no encontrado"))
        }

        /*val user = userDao.getByEmail(email)
        return if(user != null && user.password == pass){
            Result.success(user)
        }
        else{
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }*/
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
        }
        catch (e: HttpException){
            if (e.code() == 404){
                userService.addUser(UserDto(0, name, email, pass))
                return Result.success(1)
            }
            return Result.failure(e)

        } catch (e: Exception){
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        //return userDao.getByEmail(email)
        return userService.getUserByEmail(email).toUser()
    }

    suspend fun getUserById(id: Long): UserEntity? {
        //return userDao.getById(id)
        return userService.getUserById(id).toUser()
    }

    suspend fun updateUser(user: UserEntity): Result<Unit> {
        try {
            userService.updateUser(user.id, user.toDto())
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }

        /*return try {
            userDao.update(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }*/
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


    suspend fun updatePassword(user: UserEntity, newPassword: String): Result<Unit> {
        val newUser = user.toDto()
        newUser.password = newPassword
        try {
            userService.updateUser(newUser.id, newUser)
            return Result.success(Unit)
        } catch (e: Exception){
            return Result.failure(e)
        }
        /*return try {
            userDao.updatePassword(userId, newPassword)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }*/
    }

}