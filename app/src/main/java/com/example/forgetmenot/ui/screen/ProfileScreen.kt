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
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by authViewModel.profile.collectAsState()

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

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
                        onClick = authViewModel::saveProfile,
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
                        onClick = authViewModel::changePassword,
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
}