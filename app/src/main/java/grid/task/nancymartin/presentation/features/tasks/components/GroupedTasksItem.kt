package grid.task.nancymartin.presentation.features.tasks.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.R
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import java.util.Calendar

/**
 * Created by nazar at 15.08.2024
 */
@Composable
fun GroupedTasksItem(
    groupedTask: GroupedTasks,
    onDoneChange: (Task, Boolean) -> Unit,
    onDeleteClicked: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = groupedTask.title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        groupedTask.tasks.forEachIndexed { index, task ->
            val gap = 8.dp
            TaskItem(
                task = task,
                onDoneChange = onDoneChange,
                onDeleteClicked = onDeleteClicked,
                modifier = Modifier
                    .padding(
                        top = if (index == 0) 0.dp else gap / 2,
                        bottom = if (index == groupedTask.tasks.count() - 1) 0.dp else gap / 2
                    )
            )
        }
    }
}

@Preview
@Composable
private fun GroupedTasksItemPreview() {
    val tasks = mutableListOf<Task>()
    repeat(3) {
        tasks.add(
            Task(
                id = tasks.count(),
                title = "Some task title",
                description = "Some task description",
                startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 2,
                endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 5,
                list = "Test list ${tasks.count()}",
                done = false
            )
        )
    }
    GridTaskTheme {
        MainScreensLayout {
            GroupedTasksItem(
                GroupedTasks(
                    title = R.string.today,
                    tasks = tasks
                ),
                onDoneChange = { _, _ -> },
                onDeleteClicked = {}
            )
        }
    }
}