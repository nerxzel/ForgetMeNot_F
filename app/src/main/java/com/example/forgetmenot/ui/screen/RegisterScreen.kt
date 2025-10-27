package com.example.forgetmenot.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.forgetmenot.viewmodel.AuthViewModel
import com.example.forgetmenot.ui.components.RegisterForm
import com.example.forgetmenot.ui.theme.LightBlueGray

@Composable
fun RegisterScreenVm(
    vm: AuthViewModel,
    onRegisteredNavigateLogin: () -> Unit,
    onGoLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by vm.register.collectAsStateWithLifecycle()

    if (state.success) {
        vm.clearRegisterResult()
        onRegisteredNavigateLogin()
    }

    RegisterScreen(
        name = state.name,
        email = state.email,
        pass = state.pass,
        confirm = state.confirm,
        nameError = state.nameError,
        emailError = state.emailError,
        passError = state.passError,
        confirmError = state.confirmError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onNameChange = vm::onNameChange,
        onEmailChange = vm::onRegisterEmailChange,
        onPassChange = vm::onRegisterPassChange,
        onConfirmChange = vm::onConfirmChange,
        onSubmit = vm::submitRegister,
        onGoLogin = onGoLogin,
        modifier = modifier
    )
}

@Composable
private fun RegisterScreen(
    name: String,
    email: String,
    pass: String,
    confirm: String,
    nameError: String?,
    emailError: String?,
    passError: String?,
    confirmError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBlueGray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            RegisterForm(
                name = name,
                email = email,
                pass = pass,
                confirm = confirm,
                nameError = nameError,
                emailError = emailError,
                passError = passError,
                confirmError = confirmError,
                canSubmit = canSubmit,
                isSubmitting = isSubmitting,
                errorMsg = errorMsg,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPassChange = onPassChange,
                onConfirmChange = onConfirmChange,
                onSubmit = onSubmit,
                onGoLogin = onGoLogin
            )
        }
    }
}