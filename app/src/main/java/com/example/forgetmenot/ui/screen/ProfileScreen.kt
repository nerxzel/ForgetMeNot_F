package com.example.forgetmenot.ui.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.forgetmenot.viewmodel.AuthViewModel
import com.example.forgetmenot.ui.components.ProfileForm
import androidx.compose.runtime.remember
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import com.example.forgetmenot.ui.components.PasswordChangeForm
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by authViewModel.profile.collectAsState()
    val context = LocalContext.current

    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    LaunchedEffect(profileState.saveSuccess) {
        if (profileState.saveSuccess) {
            Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            authViewModel.clearProfileSaveResult()
        }
    }

    LaunchedEffect(profileState.passwordChangeSuccess) {
        if (profileState.passwordChangeSuccess) {
            Toast.makeText(context, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()
            authViewModel.clearPasswordChangeResult()
        }
    }

    val transition = rememberInfiniteTransition()
    val bgColorTransition by transition.animateColor(
        initialValue = LightBlueGray,
        targetValue = reddish,
        animationSpec = infiniteRepeatable(
            animation = tween ( 60000 ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColorTransition)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text("Editar Perfil", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileForm(
                        profileState = profileState,
                        onNameChange = authViewModel::onProfileNameChange,
                        onEmailChange = authViewModel::onProfileEmailChange
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { showEmailDialog = true },
                        enabled = profileState.canSubmit && !profileState.isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Cambios de Perfil")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Cambiar Contraseña", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {
                    PasswordChangeForm(
                        profileState = profileState,
                        onCurrentPasswordChange = authViewModel::onCurrentPasswordChange,
                        onNewPasswordChange = authViewModel::onNewPasswordChange,
                        onConfirmNewPasswordChange = authViewModel::onConfirmNewPasswordChange
                    )
                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { showPasswordDialog = true },
                        enabled = profileState.canChangePassword && !profileState.isChangingPassword,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (profileState.isChangingPassword) {
                            CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                        } else {
                            Text("Cambiar Contraseña")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

        }
    }

   if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que quieres actualizar su correo?") },
            confirmButton = {
                Button(onClick = {
                    authViewModel.saveProfile()
                    showEmailDialog = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEmailDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Cambiar contraseña") },
            text = { Text("Se actualizará tu contraseña. ¿Deseas continuar?") },
            confirmButton = {
                Button(onClick = {
                    authViewModel.changePassword()
                    showPasswordDialog = false
                }) { Text("Cambiar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPasswordDialog = false }) { Text("Cancelar") }
            }
        )
    }

}