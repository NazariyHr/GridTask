package grid.task.nancymartin.presentation.features.tasks

import android.os.Parcelable
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import kotlinx.parcelize.Parcelize

@Parcelize
data class TasksScreenState(
    val groupedTasks: List<GroupedTasks> = emptyList(),
    val filteringList: String? = null,
    val lists: List<String> = emptyList()
) : Parcelable
