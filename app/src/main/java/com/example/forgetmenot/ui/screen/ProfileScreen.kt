package com.example.forgetmenot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.forgetmenot.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by authViewModel.profile.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Editar Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = profileState.name,
            onValueChange = authViewModel::onProfileNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
        )
            if (profileState.nameError != null)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = profileState.email,
            onValueChange = authViewModel::onProfileEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
            if (profileState.emailError != null)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = authViewModel::saveProfile,
            enabled = profileState.canSubmit && !profileState.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}