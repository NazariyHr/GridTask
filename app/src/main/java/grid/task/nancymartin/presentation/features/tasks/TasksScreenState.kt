package grid.task.nancymartin.presentation.features.tasks

import android.os.Parcelable
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import grid.task.nancymartin.presentation.features.tasks.ui_model.TaskDisplayState
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class TasksScreenState(
    val tasks: List<Task> = emptyList(),
    val groupedTasks: List<GroupedTasks> = emptyList(),
    val filteringList: String? = null,
    val lists: List<String> = emptyList(),
    val displayState: TaskDisplayState = TaskDisplayState.LIST,
    val selectedDayForCalendar: Long = Calendar.getInstance().timeInMillis,
    val daysForCalendarSelector: List<Long> = emptyList()
) : Parcelable
