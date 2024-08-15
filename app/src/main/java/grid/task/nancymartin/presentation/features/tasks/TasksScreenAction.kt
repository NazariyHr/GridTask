package grid.task.nancymartin.presentation.features.tasks

import grid.task.nancymartin.domain.model.Task

sealed class TasksScreenAction {
    data object CreateTestTask : TasksScreenAction()
    data class DeleteTask(val task: Task) : TasksScreenAction()
    data class ChangeTaskIsDone(val task: Task, val isDone: Boolean) : TasksScreenAction()
}