package grid.task.nancymartin.presentation.features.tasks

sealed class TasksScreenEvent {
    data class ScrollToDay(val day: Long, val withAnimation: Boolean) : TasksScreenEvent()
}