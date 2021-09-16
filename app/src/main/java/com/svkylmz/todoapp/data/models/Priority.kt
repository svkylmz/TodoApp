package com.svkylmz.todoapp.data.models

import androidx.compose.ui.graphics.Color
import com.svkylmz.todoapp.ui.theme.HighPriorityColor
import com.svkylmz.todoapp.ui.theme.LowPriorityColor
import com.svkylmz.todoapp.ui.theme.MediumPriorityColor
import com.svkylmz.todoapp.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}