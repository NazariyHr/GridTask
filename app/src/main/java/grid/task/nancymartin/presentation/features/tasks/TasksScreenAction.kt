package grid.task.nancymartin.presentation.features.tasks

import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.features.tasks.ui_model.TaskDisplayState

sealed class TasksScreenAction {
    data class DeleteTask(val task: Task) : TasksScreenAction()
    data class ChangeTaskIsDone(val task: Task, val isDone: Boolean) : TasksScreenAction()
    data class ChangeFilteringList(val list: String?) : TasksScreenAction()
    data class ChangeDisplayState(val displayState: TaskDisplayState) : TasksScreenAction()
    data class ChangeSelectedDay(val selectedDay: Long) : TasksScreenAction()
    data object SetCurrentDaySelected : TasksScreenAction()
}