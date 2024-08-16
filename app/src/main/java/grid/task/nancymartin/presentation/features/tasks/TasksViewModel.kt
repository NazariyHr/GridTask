package grid.task.nancymartin.presentation.features.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.use_case.ChangeTaskIsDoneUseCase
import grid.task.nancymartin.domain.use_case.DeleteTaskUseCase
import grid.task.nancymartin.domain.use_case.GetListsFlowUseCase
import grid.task.nancymartin.domain.use_case.GetTasksFlowUseCase
import grid.task.nancymartin.domain.utils.getToday
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getTasksFlowUseCase: GetTasksFlowUseCase,
    getListsFlowUseCase: GetListsFlowUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val changeTaskIsDoneUseCase: ChangeTaskIsDoneUseCase
) : ViewModel() {
    companion object {
        const val STATE_KEY = "state"
    }

    private var stateValue: TasksScreenState
        set(value) {
            savedStateHandle[STATE_KEY] = value
        }
        get() {
            return savedStateHandle.get<TasksScreenState>(STATE_KEY)!!
        }
    val state = savedStateHandle.getStateFlow(STATE_KEY, TasksScreenState())

    private val _events = Channel<TasksScreenEvent>()
    val events = _events.receiveAsFlow()

    private val filteringList: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        combine(
            getTasksFlowUseCase(),
            getListsFlowUseCase(),
            filteringList
        ) { tasks, lists, filteringList ->
            val groupedTasks = mutableListOf<GroupedTasks>()

            val currTimeCalendar = getToday()
            val currYear = currTimeCalendar.get(Calendar.YEAR)
            val currMonth = currTimeCalendar.get(Calendar.MONTH)
            val currWeek = currTimeCalendar.get(Calendar.WEEK_OF_MONTH)
            val currDay = currTimeCalendar.get(Calendar.DAY_OF_MONTH)

            val filteredByList =
                if (filteringList == null) tasks else tasks.filter { it.list == filteringList }
            val doneTasks = filteredByList.filter { it.done }
            val undoneTasks = filteredByList.filter { !it.done }

            val toDaysTasks = mutableListOf<Task>()
            val tomorrowTasks = mutableListOf<Task>()
            val thisWeekTasks = mutableListOf<Task>()
            val thisMonthTasks = mutableListOf<Task>()
            val thisYearTasks = mutableListOf<Task>()
            val otherYearsTasks = mutableListOf<Task>()
            val previousTasks = mutableListOf<Task>()

            undoneTasks.forEach { task ->
                val taskTimeCalendar =
                    Calendar.getInstance().apply { timeInMillis = task.startTime }
                val taskYear = taskTimeCalendar.get(Calendar.YEAR)
                val taskMonth = taskTimeCalendar.get(Calendar.MONTH)
                val taskWeek = taskTimeCalendar.get(Calendar.WEEK_OF_MONTH)
                val taskDay = taskTimeCalendar.get(Calendar.DAY_OF_MONTH)

                if (currTimeCalendar.timeInMillis >= taskTimeCalendar.timeInMillis) {
                    previousTasks.add(task)
                } else if (taskYear == currYear && taskMonth == currMonth && taskDay == currDay) {
                    toDaysTasks.add(task)
                } else if (taskYear == currYear && taskMonth == currMonth && taskDay == currDay + 1) {
                    tomorrowTasks.add(task)
                } else if (taskYear == currYear && taskMonth == currMonth && taskWeek == currWeek) {
                    thisWeekTasks.add(task)
                } else if (taskYear == currYear && taskMonth == currMonth) {
                    thisMonthTasks.add(task)
                } else if (taskYear == currYear) {
                    thisYearTasks.add(task)
                } else {
                    otherYearsTasks.add(task)
                }
            }

            if (previousTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("Previous", previousTasks))
            }
            if (toDaysTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("Today", toDaysTasks))
            }
            if (tomorrowTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("Tomorrow", tomorrowTasks))
            }
            if (thisWeekTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("This week", thisWeekTasks))
            }
            if (thisMonthTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("This month", thisMonthTasks))
            }
            if (thisYearTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("This year", thisYearTasks))
            }
            if (otherYearsTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("Other years", otherYearsTasks))
            }
            if (doneTasks.isNotEmpty()) {
                groupedTasks.add(GroupedTasks("Done tasks", doneTasks))
            }

            stateValue = stateValue.copy(
                tasks = filteredByList,
                groupedTasks = groupedTasks,
                lists = lists,
                filteringList = filteringList
            )
        }
            .launchIn(viewModelScope)
        initCalendarSelector()
    }

    private fun initCalendarSelector() {
        viewModelScope.launch {
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val todayMillis = today.timeInMillis
            val dayAfterTwoYearsMillis = today.apply { add(Calendar.YEAR, 2) }.timeInMillis

            val days = run {
                today.add(Calendar.YEAR, -3)
                val days = mutableListOf<Long>()
                while (today.timeInMillis < dayAfterTwoYearsMillis) {
                    days.add(today.timeInMillis)
                    today.add(Calendar.DAY_OF_YEAR, 1)
                }
                days
            }

            stateValue = stateValue.copy(
                selectedDayForCalendar = todayMillis,
                daysForCalendarSelector = days
            )

            _events.send(TasksScreenEvent.ScrollToDay(todayMillis, withAnimation = false))
        }
    }

    fun onAction(action: TasksScreenAction) {
        when (action) {
            is TasksScreenAction.DeleteTask -> {
                viewModelScope.launch {
                    deleteTaskUseCase(action.task)
                }
            }

            is TasksScreenAction.ChangeTaskIsDone -> {
                viewModelScope.launch {
                    changeTaskIsDoneUseCase(action.task, action.isDone)
                }
            }

            is TasksScreenAction.ChangeFilteringList -> {
                viewModelScope.launch {
                    filteringList.emit(action.list)
                }
            }

            is TasksScreenAction.ChangeDisplayState -> {
                stateValue = stateValue.copy(
                    displayState = action.displayState
                )
            }

            is TasksScreenAction.ChangeSelectedDay -> {
                stateValue = stateValue.copy(
                    selectedDayForCalendar = action.selectedDay
                )
            }

            TasksScreenAction.SetCurrentDaySelected -> {
                viewModelScope.launch {
                    val today = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    val todayMillis = today.timeInMillis
                    stateValue = stateValue.copy(
                        selectedDayForCalendar = todayMillis
                    )
                    _events.send(TasksScreenEvent.ScrollToDay(todayMillis, withAnimation = true))
                }
            }
        }
    }
}