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
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.forgetmenot.ui.components.ArticleForm
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish
import com.example.forgetmenot.viewmodel.ArticleViewModel

@Composable
fun DetailsScreen(
    articleId: Long,
    articleViewModel: ArticleViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGoCamera: () -> Unit,
    newImageUri: String?,
    onClearNewImage: () -> Unit
) {

    val context = LocalContext.current

    val state by articleViewModel.formState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val article by remember(articleId) {
        derivedStateOf { articleViewModel.getArticleById(articleId) }
    }

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val condition = remember { mutableStateOf("") }
    val purchaseDate = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val transition = rememberInfiniteTransition()
    val bgColorTransition by transition.animateColor(
        initialValue = LightBlueGray,
        targetValue = reddish,
        animationSpec = infiniteRepeatable(
            animation = tween ( 60000 ),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(article) {
        article?.let {
            articleViewModel.loadArticleIntoForm(it)
        } ?: run {

            onNavigateBack()
        }
    }

    LaunchedEffect(state) {
        name.value = state.name
        description.value = state.description
        category.value = state.category
        price.value = state.price
        condition.value = state.condition
        purchaseDate.value = state.purchaseDate
        location.value = state.location
        tags.value = state.tags
        imageUri = state.imageUrl
    }


    LaunchedEffect(newImageUri) {
        if (newImageUri != null) {
            articleViewModel.setImageUrl(newImageUri)
            onClearNewImage()
        }
    }

    if (article == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando artículo...")
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColorTransition)
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Editar Artículo", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
            ArticleForm(
                name = name,
                description = description,
                category = category,
                price = price,
                condition = condition,
                purchaseDate = purchaseDate,
                location = location,
                tags = tags
            )}

            LaunchedEffect(name.value) {
                if (name.value != state.name) articleViewModel.onNameChange(
                    name.value
                )
            }
            LaunchedEffect(description.value) {
                if (description.value != state.description) articleViewModel.onDescriptionChange(
                    description.value
                )
            }
            LaunchedEffect(category.value) {
                if (category.value != state.category) articleViewModel.onCategoryChange(
                    category.value
                )
            }
            LaunchedEffect(price.value) {
                if (price.value != state.price) articleViewModel.onPriceChange(
                    price.value
                )
            }
            LaunchedEffect(condition.value) {
                if (condition.value != state.condition) articleViewModel.onConditionChange(
                    condition.value
                )
            }
            LaunchedEffect(purchaseDate.value) {
                if (purchaseDate.value != state.purchaseDate) articleViewModel.onPurchaseDateChange(
                    purchaseDate.value
                )
            }
            LaunchedEffect(location.value) {
                if (location.value != state.location) articleViewModel.onLocationChange(
                    location.value
                )
            }
            LaunchedEffect(tags.value) {
                if (tags.value != state.tags) articleViewModel.onTagsChange(
                    tags.value
                )
            }

            Spacer(Modifier.height(16.dp))

            if (imageUri != null) {
                AsyncImage(
                    model = Uri.parse(imageUri!!),
                    contentDescription = "Foto del artículo",
                    modifier = Modifier.size(100.dp)
                )
            }
            IconButton(onClick = onGoCamera) {
                Icon(Icons.Default.CameraAlt, "Cambiar Foto")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    articleViewModel.updateArticle(articleId)
                    Toast.makeText(context, "Artículo actualizado", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                enabled = state.canSubmit,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("Guardar Cambios")
            }
                Button(
                    onClick = {
                        showDeleteDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Eliminar Artículo")
                }
            }
        }

        Spacer(Modifier.height(4.dp))
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar artículo?") },
            text = { Text("Esta acción es permanente y no se puede deshacer. ¿Desea confirmar?") },
            confirmButton = {
                Button(
                    onClick = {
                        if (article != null) {
                            articleViewModel.deleteArticle(article!!)
                            showDeleteDialog = false
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
