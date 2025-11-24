package com.example.forgetmenot.ui.screen


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.example.forgetmenot.ui.components.ArticleCard
import com.example.forgetmenot.viewmodel.ArticleViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.forgetmenot.ui.theme.LightBlueGray
import com.example.forgetmenot.ui.theme.reddish

@Composable
fun HomeScreen(
    articleViewModel: ArticleViewModel,
    email: String,
    onArticleClick: (Long) -> Unit,
    onAddItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(email) {
        if (email.isNotBlank()) {
            articleViewModel.loadAllArticles(email)
        }
    }
    val bg = MaterialTheme.colorScheme.background

    val articles by articleViewModel.articles.collectAsState()

    val transition = rememberInfiniteTransition()
    val bgColorTransition by transition.animateColor(
        initialValue = LightBlueGray,
        targetValue = reddish,
        animationSpec = infiniteRepeatable(
            animation = tween ( 60000 ),
            repeatMode = RepeatMode.Reverse
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(bgColorTransition)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Mis artículos (${articles.size})")
                }
            }

        items(articles, key = { it.id }) { item ->
            ArticleCard(
                item = item,
                onClick = { onArticleClick(item.id) }
            )
        }
    }
}
