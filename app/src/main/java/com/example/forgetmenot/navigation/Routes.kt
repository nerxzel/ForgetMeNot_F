package com.example.forgetmenot.navigation

sealed class Route(val path: String) {
    data object Home     : Route("home")
    data object Login    : Route("login")
    data object Register : Route("register")
    data object Profile : Route("profile")
    data object Details  : Route("details/{articleId}")
    data object AddArticle : Route("addArticle")
    data object Camera   : Route("camera")
}
