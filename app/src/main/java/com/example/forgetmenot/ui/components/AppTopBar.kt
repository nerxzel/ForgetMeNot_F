package com.example.forgetmenot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.forgetmenot.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onGoProfile: () -> Unit,
    onLogout: () -> Unit
) {

    var showMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Box(
                    modifier = Modifier.Companion
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(6.dp),
                    contentAlignment = Alignment.Companion.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.box2),
                        contentDescription = "Icono del perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Companion.Fit
                    )
                }
            }
        },

        actions = {
            IconButton(onClick = onGoProfile) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Companion.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.user2),
                        contentDescription = "Icono del perfil",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Companion.Fit
                    )
                }
            };

            IconButton(onClick = { showLogoutDialog = true }) {
                Box(
                    modifier = Modifier.Companion
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Companion.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Icono del perfil",
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(7.dp),
                        contentScale = ContentScale.Companion.Fit
                    )
                }
            }
        }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que quieres salir?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}