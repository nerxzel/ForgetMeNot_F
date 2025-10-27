package com.example.forgetmenot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forgetmenot.data.repository.UserRepository

class AuthViewModelFactory (
    private val repository: UserRepository
): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Error desconocido: ${modelClass.name}")
    }
}