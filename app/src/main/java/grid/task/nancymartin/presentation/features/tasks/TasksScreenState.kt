package grid.task.nancymartin.presentation.features.tasks

import android.os.Parcelable
import grid.task.nancymartin.domain.model.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class TasksScreenState(
    val title: String = "Tasks",
    val tasks: List<Task> = emptyList(),
    val lists: List<String> = emptyList()
) : Parcelable
