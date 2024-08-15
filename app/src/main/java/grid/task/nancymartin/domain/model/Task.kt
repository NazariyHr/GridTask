package grid.task.nancymartin.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val list: String,
    val done: Boolean
) : Parcelable
