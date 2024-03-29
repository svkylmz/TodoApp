package com.svkylmz.todoapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LowPriorityColor = Color(0xFF35FF3D)
val MediumPriorityColor = Color(0xFFFFC107)
val HighPriorityColor = Color(0xFFF13527)
val NonePriorityColor = Color(0xFFA5A5A5)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF242424)

val Colors.splashScreenBackgroundColor: Color
    @Composable
    get() = if (isLight) Purple500 else DarkGray

val Colors.splashScreenContentColor: Color
    @Composable
    get() = if (isLight) DarkGray else MediumGray

val Colors.taskItemBackgroundColor: Color
    @Composable
    get() = if (isLight) LightGray else Color.Transparent

val Colors.taskItemTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (isLight) Color.White else LightGray

val Colors.topAppBarBackgroundColor: Color
    @Composable
    get() = if (isLight) Purple500 else DarkGray

val Colors.fabBackgroundColor: Color
    @Composable
    get() = if (isLight) Purple500 else Purple700