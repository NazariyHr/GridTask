package grid.task.nancymartin.domain.use_case

import grid.task.nancymartin.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListsFlowUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(): Flow<List<String>> =
        tasksRepository.getAllListsFlow()
}