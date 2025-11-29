package com.example.forgetmenot.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.forgetmenot.ui.screen.DetailsScreen
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.forgetmenot.ui.components.AppTopBar
import com.example.forgetmenot.ui.screen.HomeScreen
import com.example.forgetmenot.ui.screen.LoginScreenVm
import com.example.forgetmenot.ui.screen.RegisterScreenVm
import com.example.forgetmenot.viewmodel.AuthViewModel
import com.example.forgetmenot.ui.screen.ProfileScreen
import com.example.forgetmenot.ui.screen.AddArticleScreen
import com.example.forgetmenot.ui.camera.CameraScreen
import com.example.forgetmenot.viewmodel.ArticleViewModel

@Composable
fun AppNavGraph(navController: NavHostController,
                authViewModel: AuthViewModel,
                articleViewModel: ArticleViewModel) {
    val goHome: () -> Unit    = { navController.navigate(Route.Home.path) }
    val goLogin: () -> Unit   = {
        authViewModel.resetLoginForm()
        navController.navigate(Route.Login.path) }

    val goRegister: () -> Unit = {
        authViewModel.resetRegisterForm()
        navController.navigate(Route.Register.path) }

    val goProfile: () -> Unit = { navController.navigate(Route.Profile.path) }
    val doLogout: () -> Unit = {
        authViewModel.clearUserSession()
        navController.navigate(Route.Login.path) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true } }

    val onArticleClick: (Long) -> Unit = { articleId ->
        navController.navigate("details/$articleId")
    }

    val onAddItemClick: () -> Unit = {
        articleViewModel.clearForm()
        navController.navigate(Route.AddArticle.path)
    }
    val onNavigateBack: () -> Unit = {
        navController.popBackStack()
    }
    val onGoCamera: () -> Unit = {
        navController.navigate(Route.Camera.path)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showScaffold = when (currentRoute) {
        Route.Home.path -> true
        Route.Profile.path -> true
        else -> false
    }
        Scaffold(
            topBar = {
                if (showScaffold) {
                    AppTopBar(
                       onGoProfile = goProfile,
                        onLogout = doLogout
                    )
                }
            },

            floatingActionButton = {
                if (currentRoute == Route.Home.path) {
                    FloatingActionButton(onClick = onAddItemClick) {
                        Icon(Icons.Filled.Add, "Añadir item")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Route.Login.path,
                modifier = Modifier
            ) {
                composable(Route.Home.path) {
                    val loginState by authViewModel.login.collectAsState()
                    val profileState by authViewModel.profile.collectAsState()
                    // Use profile email if available (session restored), otherwise login email
                    val currentUserId = profileState.id.toString()

                    HomeScreen(
                        articleViewModel = articleViewModel,
                        currentUserId = currentUserId,
                        onArticleClick = onArticleClick,
                        onAddItemClick = onAddItemClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                composable(Route.Login.path) {
                    LoginScreenVm(
                        vm = authViewModel,
                        onLoginOkNavigateHome = goHome,
                        onGoRegister = goRegister,
                        modifier = Modifier
                    )
                }
                composable(Route.Register.path) {
                    RegisterScreenVm(
                        vm = authViewModel,
                        onRegisteredNavigateLogin = goLogin,
                        onGoLogin = goLogin,
                        modifier = Modifier
                    )
                }

                composable(
                    route = Route.Details.path,
                    arguments = listOf(navArgument("articleId") { type = NavType.LongType })
                ) { backStackEntry ->

                    val articleId = backStackEntry.arguments?.getLong("articleId") ?: 0L
                    val article = articleViewModel.getArticleById(articleId)

                    val newImageUri = backStackEntry.savedStateHandle
                        .getLiveData<String>("newImageUri")
                        .observeAsState()

                    DetailsScreen(
                        articleId = articleId,
                        articleViewModel = articleViewModel,
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.padding(innerPadding),
                        onGoCamera = onGoCamera,
                        newImageUri = newImageUri.value,
                        onClearNewImage = {
                            backStackEntry.savedStateHandle.remove<String>("newImageUri")
                        }
                    )
                }

                composable(Route.AddArticle.path) { backStackEntry ->

                    val newImageUri = backStackEntry.savedStateHandle
                        .getLiveData<String>("newImageUri")
                        .observeAsState()

                    LaunchedEffect(Unit) {
                        articleViewModel.clearForm()
                    }

                    AddArticleScreen(
                        articleViewModel = articleViewModel,
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.padding(innerPadding),
                        onGoCamera = onGoCamera,
                        newImageUri = newImageUri.value,
                        onClearNewImage = {
                            backStackEntry.savedStateHandle.remove<String>("newImageUri")
                        }
                    )
                }

                composable(Route.Profile.path) {
                    ProfileScreen(
                        authViewModel = authViewModel,
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                composable(Route.Camera.path) { backStackEntry ->
                    CameraScreen(
                        modifier = Modifier.padding(innerPadding),
                        onPhotoTaken = { uri ->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("newImageUri", uri.toString())

                            navController.popBackStack()
                        }
                    )
                }

            }
        }
    }
