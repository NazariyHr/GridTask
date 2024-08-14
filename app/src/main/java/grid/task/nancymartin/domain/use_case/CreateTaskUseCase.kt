package grid.task.nancymartin.domain.use_case

import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.repository.TasksRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(task: Task) {
        tasksRepository.createTask(task)
    }
}