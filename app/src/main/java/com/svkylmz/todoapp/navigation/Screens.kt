package com.svkylmz.todoapp.navigation

import androidx.navigation.NavHostController
import com.svkylmz.todoapp.util.Action
import com.svkylmz.todoapp.util.Constants.LIST_SCREEN
import com.svkylmz.todoapp.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION}") {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }
    val list: (Action) -> Unit = { action ->
        navController.navigate(route = "list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
    val task: (Int) -> Unit = { taskId ->
        navController.navigate(route = "task/${taskId}")
    }
}