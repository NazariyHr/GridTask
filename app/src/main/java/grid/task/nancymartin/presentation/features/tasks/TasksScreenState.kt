package grid.task.nancymartin.presentation.features.tasks

import android.os.Parcelable
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import kotlinx.parcelize.Parcelize

@Parcelize
data class TasksScreenState(
    val title: String = "Tasks",
    val tasks: List<Task> = emptyList(),
    val groupedTasks: List<GroupedTasks> = emptyList(),
    val lists: List<String> = emptyList()
) : Parcelable
