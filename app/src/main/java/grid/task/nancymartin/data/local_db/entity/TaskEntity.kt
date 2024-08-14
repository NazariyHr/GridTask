package grid.task.nancymartin.data.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import grid.task.nancymartin.domain.model.Task

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val list: String
)

fun TaskEntity.toTask(): Task {
    return Task(
        id,
        name,
        startTime,
        endTime,
        list
    )
}

fun Task.toRateEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        name = name,
        startTime = startTime,
        endTime = endTime,
        list = list
    )
}