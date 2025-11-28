package com.example.forgetmenot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forgetmenot.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.forgetmenot.domain.validation.*
import com.example.forgetmenot.data.local.model.user.UserEntity

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val emailError: String? = null,
    val passError: String? = null,
    val isSubmitting: Boolean = false,
    val canSubmit: Boolean = false,
    val success: Boolean = false,
    val errorMsg: String? = null
)

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val pass: String = "",
    val confirm: String = "",

    val nameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val confirmError: String? = null,

    val isSubmitting: Boolean = false,
    val canSubmit: Boolean = false,
    val success: Boolean = false,
    val errorMsg: String? = null
)

data class ProfileUiState(
    val id: Long = 0L,
    val name: String = "",
    val email: String = "",

    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    val isSubmitting: Boolean = false,
    val canSubmit: Boolean = false,
    val isChangingPassword: Boolean = false,
    val canChangePassword: Boolean = false,
    val saveSuccess: Boolean = false,
    val passwordChangeSuccess: Boolean = false,

    val errorMsg: String? = null,
    val passwordChangeErrorMsg: String? = null,

    val nameError: String? = null,
    val emailError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null
)

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login

    private val _register = MutableStateFlow(RegisterUiState())
    val register: StateFlow<RegisterUiState> = _register

    private val _profile = MutableStateFlow(ProfileUiState())
    val profile: StateFlow<ProfileUiState> = _profile

    fun onLoginEmailChange(value: String) {
        _login.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeLoginCanSubmit()
    }

    fun onLoginPassChange(value: String) {
        _login.update { it.copy(pass = value) }
        recomputeLoginCanSubmit()
    }

    private fun recomputeLoginCanSubmit() {
        val s = _login.value
        val can = s.emailError == null &&
                s.email.isNotBlank() &&
                s.pass.isNotBlank()
        _login.update { it.copy(canSubmit = can) }
    }

    fun submitLogin() {
        val s = _login.value
        if (!s.canSubmit || s.isSubmitting) return
        viewModelScope.launch {
            _login.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }
            delay(500)

            val result = repository.login(s.email, s.pass)
            _login.update {
                if(result.isSuccess){
                    loadCurrentUser(s.email)
                    it.copy(isSubmitting = false, success = true, errorMsg = null)
                }
                else{
                    it.copy(isSubmitting = false, success = false,
                        errorMsg = result.exceptionOrNull()?.message ?: "Error de autenticación")
                }
            }
        }
    }

    fun clearUserSession() {
        _profile.value = ProfileUiState()
        _login.value = LoginUiState()
    }

    fun clearLoginResult() {
        _login.update { it.copy(success = false, errorMsg = null) }
    }

    fun onNameChange(value: String) {
        val filtered = value.filter { it.isLetter() || it.isWhitespace() }
        _register.update {
            it.copy(name = filtered, nameError = validateNameLettersOnly(filtered))
        }
        recomputeRegisterCanSubmit()
    }

    fun onRegisterEmailChange(value: String) {
        _register.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeRegisterCanSubmit()
    }

    fun onRegisterPassChange(value: String) {
        _register.update { it.copy(pass = value, passError = validateStrongPassword(value)) }
        _register.update { it.copy(confirmError = validateConfirm(it.pass, it.confirm)) }
        recomputeRegisterCanSubmit()
    }

    fun onConfirmChange(value: String) {
        _register.update { it.copy(confirm = value, confirmError = validateConfirm(it.pass, value)) }
        recomputeRegisterCanSubmit()
    }

    private fun recomputeRegisterCanSubmit() {
        val s = _register.value
        val noErrors = listOf(s.nameError, s.emailError, s.passError, s.confirmError).all { it == null }
        val filled = s.name.isNotBlank() && s.email.isNotBlank() && s.pass.isNotBlank() && s.confirm.isNotBlank()
        _register.update { it.copy(canSubmit = noErrors && filled) }
    }

    fun submitRegister() {
        val s = _register.value
        if (!s.canSubmit || s.isSubmitting) return
        viewModelScope.launch {
            _register.update { it.copy(isSubmitting = true, errorMsg = null, success = false) }
            delay(700)

            val result = repository.register(
                s.name,s.email,s.pass
            )

            _register.update {
                if(result.isSuccess){
                    it.copy(isSubmitting = false, success = true, errorMsg = null)
                }else{
                    it.copy(isSubmitting = false, success = false,
                        errorMsg = result.exceptionOrNull()?.message ?: "Nose pudo registrar")
                }

            }
        }
    }

    fun clearRegisterResult() {
        _register.update { it.copy(success = false, errorMsg = null) }
    }

    private fun recomputeProfileCanSubmit() {
        val s = _profile.value
        val noErrors = listOf(s.nameError, s.emailError).all { it == null }
        val filled = s.name.isNotBlank() && s.email.isNotBlank()
        _profile.update { it.copy(canSubmit = noErrors && filled) }
    }

    private fun loadCurrentUser(email: String) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null) {
                _profile.update {
                    it.copy(
                        id = user.id,
                        name = user.name,
                        email = user.email
                    )
                }
            }
        }
    }

    fun onProfileNameChange(value: String) {
        val filtered = value.filter { it.isLetter() || it.isWhitespace() }
        _profile.update {
            it.copy(name = filtered, nameError = validateNameLettersOnly(filtered))
        }
        recomputeProfileCanSubmit()
    }

    fun onProfileEmailChange(value: String) {
        _profile.update { it.copy(email = value, emailError = validateEmail(value)) }
        recomputeProfileCanSubmit()
    }

    fun saveProfile() {
        val s = _profile.value
        if (!s.canSubmit || s.isSubmitting) return
        viewModelScope.launch {
            _profile.update { it.copy(isSubmitting = true, errorMsg = null, saveSuccess = false) }

            val existingUser = repository.getUserById(s.id)
            if (existingUser == null) {
                _profile.update { it.copy(isSubmitting = false, errorMsg = "Error: Usuario no encontrado") }
                return@launch
            }

            val updatedUser = existingUser.copy(
                name = s.name,
                email = s.email
            )


            val result = repository.updateUser(updatedUser)

            _profile.update {
                if(result.isSuccess){
                    it.copy(isSubmitting = false, saveSuccess = true, errorMsg = null)
                } else {
                    it.copy(isSubmitting = false, saveSuccess = false, errorMsg = "No se pudo actualizar")
                }
            }
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _profile.update { it.copy(currentPassword = value, currentPasswordError = validateNotBlank(value, "Contraseña Actual")) }
        recomputePasswordCanSubmit()
    }
    fun onNewPasswordChange(value: String) {
        _profile.update { it.copy(newPassword = value, newPasswordError = validateStrongPassword(value)) }
        _profile.update { it.copy(confirmNewPasswordError = validateConfirm(it.newPassword, it.confirmNewPassword)) }
        recomputePasswordCanSubmit()
    }
    fun onConfirmNewPasswordChange(value: String) {
        _profile.update { it.copy(confirmNewPassword = value, confirmNewPasswordError = validateConfirm(it.newPassword, value)) }
        recomputePasswordCanSubmit()
    }
    private fun recomputePasswordCanSubmit() {
        val s = _profile.value
        val noErrors = listOf(s.currentPasswordError, s.newPasswordError, s.confirmNewPasswordError).all { it == null }
        val filled = s.currentPassword.isNotBlank() && s.newPassword.isNotBlank() && s.confirmNewPassword.isNotBlank()
        _profile.update { it.copy(canChangePassword = noErrors && filled) }
    }
    fun changePassword() {
        val s = _profile.value
        if (!s.canChangePassword || s.isChangingPassword) return
        viewModelScope.launch {
            _profile.update { it.copy(isChangingPassword = true, passwordChangeErrorMsg = null, passwordChangeSuccess = false) }
            delay(500)


            //val verifyResult = repository.verifyPassword(s.id, s.currentPassword)

            //if (verifyResult.isSuccess) {

                val updateResult = repository.updatePassword(s.id, s.newPassword)
                _profile.update {
                    if (updateResult.isSuccess) {
                        it.copy(
                            isChangingPassword = false, passwordChangeSuccess = true,
                            currentPassword = "", newPassword = "", confirmNewPassword = ""
                        )
                    } else {
                        it.copy(isChangingPassword = false, passwordChangeErrorMsg = "Error al guardar la nueva contraseña.")
                    }
                }
            //} else {
                //_profile.update { it.copy(isChangingPassword = false, passwordChangeErrorMsg = verifyResult.exceptionOrNull()?.message ?: "La contraseña actual es incorrecta.") }
            //}
        }
    }
    fun clearPasswordChangeResult() {
        _profile.update { it.copy(passwordChangeSuccess = false, passwordChangeErrorMsg = null) }
    }


}