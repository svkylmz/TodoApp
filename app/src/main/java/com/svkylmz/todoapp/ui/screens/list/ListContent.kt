package com.svkylmz.todoapp.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.svkylmz.todoapp.R
import com.svkylmz.todoapp.data.models.Priority
import com.svkylmz.todoapp.data.models.TodoTask
import com.svkylmz.todoapp.ui.theme.*
import com.svkylmz.todoapp.util.Action
import com.svkylmz.todoapp.util.RequestState
import com.svkylmz.todoapp.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListContent(
    allTasks: RequestState<List<TodoTask>>,
    searchedTasks: RequestState<List<TodoTask>>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    lowPriorityTasks: List<TodoTask>,
    highPriorityTasks: List<TodoTask>,
    sortState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    DisplayContent(
        allTasks = allTasks,
        searchedTasks = searchedTasks,
        onSwipeToDelete= onSwipeToDelete,
        lowPriorityTasks = lowPriorityTasks,
        highPriorityTasks = highPriorityTasks,
        sortState = sortState,
        searchAppBarState = searchAppBarState,
        navigateToTaskScreen = navigateToTaskScreen
    )
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayContent(
    allTasks: RequestState<List<TodoTask>>,
    searchedTasks: RequestState<List<TodoTask>>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    lowPriorityTasks: List<TodoTask>,
    highPriorityTasks: List<TodoTask>,
    sortState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                            tasks = searchedTasks.data,
                            onSwipeToDelete = onSwipeToDelete,
                            navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                            tasks = allTasks.data,
                            onSwipeToDelete = onSwipeToDelete,
                            navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                        tasks = lowPriorityTasks,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                        tasks = highPriorityTasks,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    tasks: List<TodoTask>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isNotEmpty()) {
        LazyColumn {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                val dismissState = rememberDismissState()
                val dismissDirection = dismissState.dismissDirection
                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)

                if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                    val scope = rememberCoroutineScope()
                    scope.launch {
                        delay(300)
                        onSwipeToDelete(Action.DELETE, task)
                    }
                }

                val degrees by animateFloatAsState(
                        targetValue =
                            if (dismissState.targetValue == DismissValue.Default) 0f
                            else -45f
                )

                var itemAppeared by remember { mutableStateOf(false) }
                LaunchedEffect(key1 = true) {
                    itemAppeared = true
                }

                AnimatedVisibility(
                        visible = itemAppeared && !isDismissed,
                        enter = expandVertically(
                                animationSpec = tween(durationMillis = 300)
                        ),
                        exit = shrinkVertically(
                                animationSpec = tween(durationMillis = 300)
                        )
                ) {
                    SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { FractionalThreshold(0.2f) },
                            background = { RedBackgroundForSwipe(degrees = degrees) },
                            dismissContent = {
                                TaskItem(
                                        todoTask = task,
                                        navigateToTaskScreen = navigateToTaskScreen
                                )
                            }
                    )
                }
            }
        }
    } else {
        EmptyContent()
    }
}

@Composable
fun RedBackgroundForSwipe(degrees: Float) {
    Box(modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = LARGEST_PADDING),
            contentAlignment = Alignment.CenterEnd
    ) {
        Icon(modifier = Modifier
                .rotate(degrees = degrees),
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(id = R.string.delete_action),
                tint = Color.White
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun TaskItem(
    todoTask: TodoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        onClick = { navigateToTaskScreen(todoTask.id) }
    ) {
        Column(
            modifier = Modifier
                    .padding(all = LARGE_PADDING)
                    .fillMaxWidth()
        ) {
            Row {
               Text(
                   modifier = Modifier.weight(8f),
                   text = todoTask.title,
                   color = MaterialTheme.colors.taskItemTextColor,
                   style = MaterialTheme.typography.h5,
                   fontWeight = FontWeight.Bold,
                   maxLines = 1
               )
                Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE)
                    ) {
                        drawCircle(color = todoTask.priority.color)
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = todoTask.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // "..." at the end of the text
            )
        }
    }
}
