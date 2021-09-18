package com.svkylmz.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.svkylmz.todoapp.navigation.destinations.listComposable
import com.svkylmz.todoapp.navigation.destinations.taskComposable
import com.svkylmz.todoapp.ui.viewmodels.SharedViewModel
import com.svkylmz.todoapp.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }
    NavHost(
        navController = navController,
        startDestination = LIST_SCREEN
    ) {
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(navigateToListScreen = screen.list)
    }
}