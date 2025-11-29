package com.example.forgetmenot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.forgetmenot.data.repository.UserRepository
import com.example.forgetmenot.navigation.AppNavGraph
import com.example.forgetmenot.viewmodel.AuthViewModel
import com.example.forgetmenot.viewmodel.ArticleViewModel
import com.example.forgetmenot.viewmodel.AuthViewModelFactory
import com.example.forgetmenot.ui.theme.ForgetMeNot_Theme
import com.example.forgetmenot.viewmodel.ArticleViewModelFactory
import androidx.compose.runtime.remember
import com.example.forgetmenot.data.repository.ArticleRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {

    val repository = remember { UserRepository() }
    val authViewModel: AuthViewModel = viewModel (
        factory = AuthViewModelFactory(repository)
    )

    val articleRepository = remember { ArticleRepository() }
    val articleViewModel: ArticleViewModel = viewModel(
        factory = ArticleViewModelFactory(articleRepository)
    )


    val navController = rememberNavController()
    ForgetMeNot_Theme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppNavGraph(navController = navController,
                authViewModel = authViewModel,
                articleViewModel = articleViewModel)
        }
    }
}