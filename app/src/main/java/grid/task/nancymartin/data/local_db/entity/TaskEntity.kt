package grid.task.nancymartin.data.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import grid.task.nancymartin.domain.model.Task

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val list: String,
    val done: Boolean
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        list = list,
        done = done
    )
}

fun Task.toRateEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        list = list,
        done = done
    )
}