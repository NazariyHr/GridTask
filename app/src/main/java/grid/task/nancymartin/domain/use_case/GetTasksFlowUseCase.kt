package grid.task.nancymartin.domain.use_case

import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksFlowUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(): Flow<List<Task>> =
        tasksRepository.getAllTasksFlow()
}