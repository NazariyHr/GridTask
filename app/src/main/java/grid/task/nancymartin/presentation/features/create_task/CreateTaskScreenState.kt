package grid.task.nancymartin.presentation.features.create_task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateTaskScreenState(
    val title: String = "Create Task"
) : Parcelable
