package grid.task.nancymartin.presentation.features.tasks.components.calendar

import grid.task.nancymartin.domain.model.Task

data class TaskDrawInfo(
    val task: Task,
    val yStartOfTask: Float,
    val yEndOfTask: Float
)
