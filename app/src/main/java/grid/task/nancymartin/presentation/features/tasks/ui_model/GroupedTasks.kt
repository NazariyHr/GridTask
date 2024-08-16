package grid.task.nancymartin.presentation.features.tasks.ui_model

import android.os.Parcelable
import androidx.annotation.StringRes
import grid.task.nancymartin.domain.model.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupedTasks(
    @StringRes val title: Int,
    val tasks: List<Task>
) : Parcelable