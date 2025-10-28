package com.example.forgetmenot.ui.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.forgetmenot.viewmodel.AuthViewModel
import com.example.forgetmenot.R
import com.example.forgetmenot.ui.components.LoginForm
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish

@Composable
fun LoginScreenVm(
    vm: AuthViewModel,
    onLoginOkNavigateHome: () -> Unit,
    onGoRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by vm.login.collectAsStateWithLifecycle()

    if (state.success) {
        vm.clearLoginResult()
        onLoginOkNavigateHome()
    }

    LoginScreen(
        email = state.email,
        pass = state.pass,
        emailError = state.emailError,
        passError = state.passError,
        canSubmit = state.canSubmit,
        isSubmitting = state.isSubmitting,
        errorMsg = state.errorMsg,
        onEmailChange = vm::onLoginEmailChange,
        onPassChange = vm::onLoginPassChange,
        onSubmit = vm::submitLogin,
        onGoRegister = onGoRegister,
        modifier = modifier
    )
}

@Composable
private fun LoginScreen(
    email: String,
    pass: String,
    emailError: String?,
    passError: String?,
    canSubmit: Boolean,
    isSubmitting: Boolean,
    errorMsg: String?,
    onEmailChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = MaterialTheme.colorScheme.background

    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = LightBlueGray,
        targetValue = reddish,
        animationSpec = infiniteRepeatable(
            animation = tween ( 1500 ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.fmn),
                contentDescription = "Imagen de perfil circular",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            LoginForm(
                email = email,
                pass = pass,
                emailError = emailError,
                passError = passError,
                canSubmit = canSubmit,
                isSubmitting = isSubmitting,
                errorMsg = errorMsg,
                onEmailChange = onEmailChange,
                onPassChange = onPassChange,
                onSubmit = onSubmit,
                onGoRegister = onGoRegister
            )
        }
    }
}


