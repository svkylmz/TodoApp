package com.svkylmz.todoapp.ui.screens.task

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.svkylmz.todoapp.R
import com.svkylmz.todoapp.data.models.TodoTask
import com.svkylmz.todoapp.ui.theme.topAppBarBackgroundColor
import com.svkylmz.todoapp.ui.theme.topAppBarContentColor
import com.svkylmz.todoapp.util.Action

@Composable
fun TaskAppBar(
    selectedTask: TodoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    DisplayTaskAppBar(
        selectedTask = selectedTask,
        navigateToListScreen = navigateToListScreen
    )
}

@Composable
fun DisplayTaskAppBar(
    selectedTask: TodoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}

@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_action),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.add_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            DeleteAction(onDeleteClicked = navigateToListScreen)
            UpdateAction(onUpdateClicked = navigateToListScreen)
        }
    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Action) -> Unit
) {
    IconButton(onClick = { onDeleteClicked(Action.DELETE) }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.update_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}