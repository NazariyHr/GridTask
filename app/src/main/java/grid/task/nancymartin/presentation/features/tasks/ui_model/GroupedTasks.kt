package grid.task.nancymartin.presentation.features.tasks.ui_model

import android.os.Parcelable
import grid.task.nancymartin.domain.model.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupedTasks(
    val title: String,
    val tasks: List<Task>
) : Parcelable