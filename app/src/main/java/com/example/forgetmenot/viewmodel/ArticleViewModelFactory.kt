package com.example.forgetmenot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forgetmenot.data.repository.ArticleRepository // Import your ArticleRepository

/**
 * Factory for creating instances of ArticleViewModel with dependencies.
 *
 * @param repository The ArticleRepository instance required by the ViewModel.
 */
@Suppress("UNCHECKED_CAST") // Standard practice for ViewModel factories
class ArticleViewModelFactory(
    private val repository: ArticleRepository // Takes the repository as an argument
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel is ArticleViewModel
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            // If yes, create and return an instance, passing the repository
            return ArticleViewModel(repository) as T
        }
        // If not, throw an exception (standard practice)
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}