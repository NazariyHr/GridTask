package grid.task.nancymartin.presentation.features.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.use_case.ChangeTaskIsDoneUseCase
import grid.task.nancymartin.domain.use_case.CreateTaskUseCase
import grid.task.nancymartin.domain.use_case.DeleteTaskUseCase
import grid.task.nancymartin.domain.use_case.GetListsFlowUseCase
import grid.task.nancymartin.domain.use_case.GetTasksFlowUseCase
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getTasksFlowUseCase: GetTasksFlowUseCase,
    getListsFlowUseCase: GetListsFlowUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
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

    init {
        getTasksFlowUseCase()
            .onEach { tasks ->
                stateValue = stateValue.copy(
                    tasks = tasks
                )
                val groupedTasks = mutableListOf<GroupedTasks>()

                val currTimeCalendar = Calendar.getInstance()
                val currYear = currTimeCalendar.get(Calendar.YEAR)
                val currMonth = currTimeCalendar.get(Calendar.MONTH)
                val currWeek = currTimeCalendar.get(Calendar.WEEK_OF_MONTH)
                val currDay = currTimeCalendar.get(Calendar.DAY_OF_MONTH)

                val doneTasks = tasks.filter { it.done }
                val undoneTasks = tasks.filter { !it.done }

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
                    groupedTasks = groupedTasks
                )
            }
            .launchIn(viewModelScope)
        getListsFlowUseCase()
            .onEach { lists ->
                stateValue = stateValue.copy(
                    lists = lists
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TasksScreenAction) {
        when (action) {
            TasksScreenAction.CreateTestTask -> {
                viewModelScope.launch {
                    createTaskUseCase.invoke(
                        Task(
                            id = 0,
                            title = "Some task title",
                            description = "Some task description",
                            startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 2,
                            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 5,
                            list = "Test list ${stateValue.lists.count()}",
                            done = false
                        )
                    )
                }
            }

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
        }
    }
}