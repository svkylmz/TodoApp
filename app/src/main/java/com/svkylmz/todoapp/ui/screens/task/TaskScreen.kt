package com.svkylmz.todoapp.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.svkylmz.todoapp.data.models.TodoTask
import com.svkylmz.todoapp.util.Action

@Composable
fun TaskScreen(
    selectedTask: TodoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
        },
        content = {}
    )
}