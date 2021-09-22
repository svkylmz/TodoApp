package com.svkylmz.todoapp.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.svkylmz.todoapp.R
import com.svkylmz.todoapp.ui.theme.SPLASH_LOGO_HEIGHT
import com.svkylmz.todoapp.ui.theme.splashScreenBackgroundColor
import com.svkylmz.todoapp.ui.theme.splashScreenContentColor
import com.svkylmz.todoapp.util.Constants.SPLASH_SCREEN_DELAY
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigateToListScreen: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(SPLASH_SCREEN_DELAY)  //show the splash screen for 3 seconds
        navigateToListScreen()
    }
    Box(
        modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.splashScreenBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(SPLASH_LOGO_HEIGHT),
            painter = painterResource(id = R.drawable.ic_to_do),
            contentDescription = stringResource(id = R.string.todo_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.splashScreenContentColor)
        )
    }
}