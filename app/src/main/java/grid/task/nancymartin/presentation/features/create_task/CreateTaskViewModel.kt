package grid.task.nancymartin.presentation.features.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.use_case.CreateTaskUseCase
import grid.task.nancymartin.domain.use_case.GetListsFlowUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createTaskUseCase: CreateTaskUseCase,
    getListsFlowUseCase: GetListsFlowUseCase
) : ViewModel() {
    companion object {
        const val STATE_KEY = "state"
    }

    private var stateValue: CreateTaskScreenState
        set(value) {
            savedStateHandle[STATE_KEY] = value
        }
        get() {
            return savedStateHandle.get<CreateTaskScreenState>(STATE_KEY)!!
        }
    val state = savedStateHandle.getStateFlow(STATE_KEY, CreateTaskScreenState())

    init {
        getListsFlowUseCase()
            .onEach { lists ->
                stateValue = stateValue.copy(
                    lists = lists
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CreateTaskScreenAction) {
        when (action) {
            is CreateTaskScreenAction.CreateTestTask -> {
                viewModelScope.launch {
                    createTaskUseCase.invoke(
                        Task(
                            id = 0,
                            title = action.title,
                            description = action.description,
                            startTime = action.startTime,
                            endTime = action.endTime,
                            list = action.list,
                            done = false
                        )
                    )
                }
            }
        }
    }
}