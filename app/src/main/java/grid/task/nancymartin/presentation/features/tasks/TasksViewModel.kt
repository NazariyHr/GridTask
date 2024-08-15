package grid.task.nancymartin.presentation.features.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.use_case.CreateTaskUseCase
import grid.task.nancymartin.domain.use_case.DeleteTaskUseCase
import grid.task.nancymartin.domain.use_case.GetListsFlowUseCase
import grid.task.nancymartin.domain.use_case.GetTasksFlowUseCase
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
    private val deleteTaskUseCase: DeleteTaskUseCase
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
                            list = "Test list ${stateValue.lists.count()}"
                        )
                    )
                }
            }

            is TasksScreenAction.DeleteTask -> {
                viewModelScope.launch {
                    deleteTaskUseCase(action.task)
                }
            }
        }
    }
}