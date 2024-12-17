package grid.task.nancymartin.presentation.features.create_task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateTaskScreenState(
    val lists: List<String> = emptyList()
) : Parcelable
