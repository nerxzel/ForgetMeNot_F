package com.example.forgetmenot.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.forgetmenot.ui.components.ArticleForm
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish
import com.example.forgetmenot.viewmodel.ArticleViewModel

@Composable
fun AddArticleScreen(
    articleViewModel: ArticleViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGoCamera: () -> Unit,
    newImageUri: String?,
    onClearNewImage: () -> Unit
) {

    val state by articleViewModel.formState.collectAsState()

    val name = remember { mutableStateOf(state.name) }
    val description = remember { mutableStateOf(state.description) }
    val category = remember { mutableStateOf(state.category) }
    val price = remember { mutableStateOf(state.price) }
    val condition = remember { mutableStateOf(state.condition) }
    val purchaseDate = remember { mutableStateOf(state.purchaseDate) }
    val location = remember { mutableStateOf(state.location) }
    val tags = remember { mutableStateOf(state.tags) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val transition = rememberInfiniteTransition()
    val bgColorTransition by transition.animateColor(
        initialValue = LightBlueGray,
        targetValue = reddish,
        animationSpec = infiniteRepeatable(
            animation = tween(60000),
            repeatMode = RepeatMode.Reverse
        )
    )

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

    Box(
        modifier = modifier // Apply padding from Scaffold first
            .fillMaxSize()
            .background(bgColorTransition) // Animated background here
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Añadir Nuevo Artículo", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // White background
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
                )
            }
            LaunchedEffect(name.value) { articleViewModel.onNameChange(name.value) }
            LaunchedEffect(description.value) { articleViewModel.onDescriptionChange(description.value) }
            LaunchedEffect(category.value) { articleViewModel.onCategoryChange(category.value) }
            LaunchedEffect(price.value) { articleViewModel.onPriceChange(price.value) }
            LaunchedEffect(condition.value) { articleViewModel.onConditionChange(condition.value) }
            LaunchedEffect(purchaseDate.value) { articleViewModel.onPurchaseDateChange(purchaseDate.value) }
            LaunchedEffect(location.value) { articleViewModel.onLocationChange(location.value) }
            LaunchedEffect(tags.value) { articleViewModel.onTagsChange(tags.value) }

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
            Button(
                onClick = {
                    articleViewModel.addArticle()
                    onNavigateBack()
                },
                enabled = state.canSubmit,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Guardar Artículo")
            }}
        }
    }
}