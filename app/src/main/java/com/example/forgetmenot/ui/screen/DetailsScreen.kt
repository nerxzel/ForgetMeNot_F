package com.example.forgetmenot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.forgetmenot.data.local.model.Article
import android.net.Uri
import com.example.forgetmenot.ui.components.ArticleForm

@Composable
fun DetailsScreen(
    article: Article?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGoCamera: () -> Unit,
    newImageUri: String?,
    onClearNewImage: () -> Unit
) {
    if (article == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Artículo no encontrado.")
        }
        return
    }

    val name = remember { mutableStateOf(article.name) }
    val description = remember { mutableStateOf(article.description) }
    val category = remember { mutableStateOf(article.category) }
    val price = remember { mutableStateOf(article.price.toString()) }
    val condition = remember { mutableStateOf(article.condition) }
    val purchaseDate = remember { mutableStateOf(article.purchaseDate) }
    val location = remember { mutableStateOf(article.location) }
    val tags = remember { mutableStateOf(article.tags.joinToString(",")) }
    var imageUri by remember { mutableStateOf(article.imageUrl) }

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
    ) {
        Text("Editar Artículo", style = MaterialTheme.typography.headlineMedium)
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


        IconButton(onClick = onGoCamera) {
            Icon(Icons.Default.CameraAlt, "Cambiar Foto")
        }

        if (imageUri != null) {
            AsyncImage(
                model = Uri.parse(imageUri),
                contentDescription = "Foto del artículo",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { /* Guardar cambios del artículo */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Cambios")
        }
    }
}