package grid.task.nancymartin.data.local_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import grid.task.nancymartin.data.local_db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("select * from taskentity ORDER BY startTime desc")
    fun getAllFlow(): Flow<List<TaskEntity>>

    @Query("select distinct list from taskentity where list != '' order by id desc")
    fun getAllListsFlow(): Flow<List<String>>
}