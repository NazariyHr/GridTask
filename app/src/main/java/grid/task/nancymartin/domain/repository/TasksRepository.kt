package grid.task.nancymartin.domain.repository

import grid.task.nancymartin.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun createTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun changeTaskIsDone(task: Task, isDone: Boolean)
    fun getAllTasksFlow(): Flow<List<Task>>
    fun getAllListsFlow(): Flow<List<String>>
}