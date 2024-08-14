package grid.task.nancymartin.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import grid.task.nancymartin.data.local_db.dao.TaskDao
import grid.task.nancymartin.data.local_db.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class TasksDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}