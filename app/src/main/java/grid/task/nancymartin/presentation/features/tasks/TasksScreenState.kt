package grid.task.nancymartin.presentation.features.tasks

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TasksScreenState(
    val title: String = "Tasks"
) : Parcelable
