package com.example.forgetmenot.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.forgetmenot.data.local.model.Article
import org.junit.Rule
import org.junit.Test

class ArticleCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun articleCard_muestraInformacionCorrecta() {
        val sampleArticle = Article(
            id = 1,
            name = "iPhone 14 Pro",
            description = "Teléfono en excelente estado",
            imageUrl = null,
            category = "Electrónicos",
            price = 1200000.0,
            condition = "Usado",
            purchaseDate = "10/10/2023",
            location = "Cajón",
            tags = listOf("celular")
        )

        composeTestRule.setContent {
            ArticleCard(
                item = sampleArticle,
                onClick = {},
            )
        }

        val precioEsperado = "$ ${"%,.0f".format(sampleArticle.price)}"

        composeTestRule.onNodeWithText("iPhone 14 Pro").assertIsDisplayed()
        composeTestRule.onNodeWithText(precioEsperado).assertIsDisplayed()
        composeTestRule.onNodeWithText("Usado").assertIsDisplayed()

        Thread.sleep(5000)
    }
}