package com.example.forgetmenot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.forgetmenot.viewmodel.ProfileUiState

@Composable
fun PasswordChangeForm(
    profileState: ProfileUiState,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = profileState.newPassword,
            onValueChange = onNewPasswordChange,
            label = { Text("Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { PasswordVisibilityToggle(showNewPassword) { showNewPassword = it } },
            isError = profileState.newPasswordError != null,
            singleLine = true
        )
        if (profileState.newPasswordError != null) {
            val errorText = profileState.newPasswordError
            Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = profileState.confirmNewPassword,
            onValueChange = onConfirmNewPasswordChange,
            label = { Text("Confirmar Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { PasswordVisibilityToggle(showConfirmPassword) { showConfirmPassword = it } },
            isError = profileState.confirmNewPasswordError != null,
            singleLine = true
        )
        if (profileState.confirmNewPasswordError != null) {
            val errorText = profileState.confirmNewPasswordError
            Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun PasswordVisibilityToggle(shown: Boolean, onToggle: (Boolean) -> Unit) {
    IconButton(onClick = { onToggle(!shown) }) {
        Icon(
            imageVector = if (shown) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (shown) "Ocultar contraseña" else "Mostrar contraseña"
        )
    }
}