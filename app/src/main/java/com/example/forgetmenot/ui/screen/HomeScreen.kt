package com.example.forgetmenot.ui.screen


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
import com.example.forgetmenot.sampleItems
import androidx.compose.foundation.lazy.items
import com.example.forgetmenot.ui.components.ArticleCard

@Composable
fun HomeScreen(
    onArticleClick: (Int) -> Unit,
    onAddItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = MaterialTheme.colorScheme.background

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Mis artículos (${sampleItems.size})")
                }
            }

            items(sampleItems) { item ->
                ArticleCard(item = item, onClick = {
                    onArticleClick(item.id)
                })
            }

            }
}
