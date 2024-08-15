package grid.task.nancymartin.data

import grid.task.nancymartin.data.local_db.TasksDatabase
import grid.task.nancymartin.data.local_db.entity.toRateEntity
import grid.task.nancymartin.data.local_db.entity.toTask
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(
    private val tasksDatabase: TasksDatabase
) : TasksRepository {
    override suspend fun createTask(task: Task) {
        tasksDatabase.taskDao.insert(task.toRateEntity())
    }

    override suspend fun deleteTask(task: Task) {
        tasksDatabase.taskDao.delete(task.toRateEntity())
    }

    override suspend fun changeTaskIsDone(task: Task, isDone: Boolean) {
        tasksDatabase.taskDao.update(task.toRateEntity().copy(done = isDone))
    }

    override fun getAllTasksFlow(): Flow<List<Task>> =
        tasksDatabase.taskDao.getAllFlow().distinctUntilChanged()
            .map { taskEntities -> taskEntities.map { taskEntity -> taskEntity.toTask() }.sortedBy { it.done } }

    override fun getAllListsFlow(): Flow<List<String>> =
        tasksDatabase.taskDao.getAllListsFlow().distinctUntilChanged()
}