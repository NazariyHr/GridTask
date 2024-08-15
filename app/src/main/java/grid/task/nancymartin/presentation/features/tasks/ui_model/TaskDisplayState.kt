package grid.task.nancymartin.presentation.features.tasks.ui_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TaskDisplayState : Parcelable {
    LIST,
    CALENDAR
}