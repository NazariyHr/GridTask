package grid.task.nancymartin.presentation.features.create_task

sealed class CreateTaskScreenAction {
    data class CreateTestTask(
        val title: String,
        val description: String,
        val startTime: Long,
        val endTime: Long,
        val list: String
    ) : CreateTaskScreenAction()
}