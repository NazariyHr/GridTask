package grid.task.nancymartin.presentation.features.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.common.theme.ColorLightContainers
import java.util.Calendar

/**
 * Created by nazar at 14.08.2024
 */
@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = ColorLightContainers,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 4.dp, horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Current time: ${Calendar.getInstance().timeInMillis}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = task.id.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = task.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = task.startTime.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = task.endTime.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = task.list,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun TaskItemPreview() {
    TaskItem(
        task = Task(
            id = 0,
            name = "Some task name",
            startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 2,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 5,
            list = "Test list 1"

        ),
        modifier = Modifier.fillMaxWidth()
    )
}