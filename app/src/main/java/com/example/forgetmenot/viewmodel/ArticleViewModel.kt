package com.example.forgetmenot.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.forgetmenot.data.local.model.Article
import com.example.forgetmenot.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.forgetmenot.domain.validation.validateNotBlank

data class ArticleFormState(
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val condition: String = "",
    val purchaseDate: String = "",
    val location: String = "",
    val tags: String = "",
    val imageUrl: String? = null,


    val nameError: String? = null,
    val locationError: String? = null,

    val canSubmit: Boolean = false

)

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _formState = MutableStateFlow(ArticleFormState())
    val formState: StateFlow<ArticleFormState> = _formState.asStateFlow()

    private var currentUserEmail: String = ""

    // Removed init block to avoid loading without email

    fun loadAllArticles(email: String) {
        currentUserEmail = email
        viewModelScope.launch {
            _articles.value = repository.getAllArticles(email)
        }
    }

    fun loadArticleIntoForm(article: Article) {
        _formState.update {
            it.copy(
                name = article.name,
                description = article.description,
                category = article.category,
                price = article.price.toString(),
                condition = article.condition,
                purchaseDate = article.purchaseDate,
                location = article.location,
                tags = article.tags.joinToString(","),
                imageUrl = article.imageUrl,
                nameError = null,
                locationError = null,
                canSubmit = true
            )
        }
    }

    fun clearForm() {
        _formState.value = ArticleFormState()
    }


    fun setImageUrl(uriString: String?) {
        _formState.update { it.copy(imageUrl = uriString) }
    }


    fun onNameChange(value: String) {
        _formState.update { it.copy(name = value, nameError = validateNotBlank(value, "Nombre")) }
        validateForm()
    }

    fun onLocationChange(value: String) {
        _formState.update { it.copy(location = value, locationError = validateNotBlank(value, "Ubicación")) }
        validateForm()
    }


    fun onDescriptionChange(value: String) { _formState.update { it.copy(description = value) } }
    fun onCategoryChange(value: String) { _formState.update { it.copy(category = value) } }
    fun onPriceChange(value: String) { _formState.update { it.copy(price = value) } }
    fun onConditionChange(value: String) { _formState.update { it.copy(condition = value) } }
    fun onPurchaseDateChange(value: String) { _formState.update { it.copy(purchaseDate = value) } }
    fun onTagsChange(value: String) { _formState.update { it.copy(tags = value) } }


    private fun validateForm() {
        _formState.update {
            it.copy(canSubmit = it.name.isNotBlank() && it.location.isNotBlank())
        }
    }


    fun addArticle() {
        if (!_formState.value.canSubmit) return
        if (currentUserEmail.isBlank()) return // Should handle error or ensure email is set

        viewModelScope.launch {
            val s = _formState.value
            val newArticle = Article(
                id = 0L,
                name = s.name,
                description = s.description,
                category = s.category,
                purchaseDate = s.purchaseDate,
                location = s.location,
                condition = s.condition,
                price = s.price.toDoubleOrNull() ?: 0.0,
                imageUrl = s.imageUrl,
                tags = s.tags.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            )

            repository.addArticle(newArticle, currentUserEmail)

            loadAllArticles(currentUserEmail)
        }
    }

    fun updateArticle(articleId: Long) {
        if (!_formState.value.canSubmit) return
        if (currentUserEmail.isBlank()) return

        viewModelScope.launch {
            val s = _formState.value
            val updatedArticle = Article(
                id = articleId,
                name = s.name,
                description = s.description,
                category = s.category,
                purchaseDate = s.purchaseDate,
                location = s.location,
                condition = s.condition,
                price = s.price.toDoubleOrNull() ?: 0.0,
                imageUrl = s.imageUrl,
                tags = s.tags.split(',').map { it.trim() }.filter { it.isNotEmpty() }
            )
            repository.updateArticle(updatedArticle, currentUserEmail)
            loadAllArticles(currentUserEmail)
        }
    }

    fun getArticleById(id: Long): Article? {
        return _articles.value.find { it.id == id }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article.id)
            loadAllArticles(currentUserEmail)
        }
    }
}

