package com.example.forgetmenot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.forgetmenot.ui.components.ArticleForm
import androidx.compose.runtime.*
import coil.compose.AsyncImage

@Composable
fun AddArticleScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGoCamera: () -> Unit,
    newImageUri: String?,
    onClearNewImage: () -> Unit
) {

    var imageUri by remember { mutableStateOf<String?>(null) }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val condition = remember { mutableStateOf("") }
    val purchaseDate = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }

    LaunchedEffect(newImageUri) {
        if (newImageUri != null) {
            imageUri = newImageUri
            onClearNewImage()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Añadir Nuevo Artículo", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        ArticleForm(
            name = name,
            description = description,
            category = category,
            price = price,
            condition = condition,
            purchaseDate = purchaseDate,
            location = location,
            tags = tags
        )

        Spacer(Modifier.height(16.dp))

        if (imageUri != null) {
            AsyncImage(
                model = Uri.parse(imageUri),
                contentDescription = "Foto del artículo",
                modifier = Modifier.size(100.dp)
            )
        }

        IconButton(onClick = onGoCamera) {
            Icon(Icons.Default.CameraAlt, "Tomar Foto")
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { /* Guardar nuevo artículo */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Artículo")
        }
    }
}