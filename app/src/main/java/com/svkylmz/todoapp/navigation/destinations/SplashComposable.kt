package com.svkylmz.todoapp.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.svkylmz.todoapp.ui.screens.splash.SplashScreen
import com.svkylmz.todoapp.util.Constants.SPLASH_SCREEN

fun NavGraphBuilder.splashComposable(
        navigateToListScreen: () -> Unit
) {
    composable(
            route = SPLASH_SCREEN
    ) {
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}