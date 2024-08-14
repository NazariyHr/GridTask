package grid.task.nancymartin.presentation.features.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.modifiers.safeSingleClick
import grid.task.nancymartin.presentation.common.theme.ColorCian
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import grid.task.nancymartin.presentation.features.tasks.components.TaskItem
import grid.task.nancymartin.presentation.navigation.Screen
import java.util.Calendar

@Composable
fun TasksScreenRoot(
    navController: NavController,
    viewModel: TasksViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TasksScreen(
        state = state,
        onCreateTaskClicked = {
            navController.navigate(Screen.CreateTask)
        },
        onAction = viewModel::onAction
    )
}

@Composable
private fun TasksScreen(
    state: TasksScreenState,
    onCreateTaskClicked: () -> Unit,
    onAction: (TasksScreenAction) -> Unit
) {
    MainScreensLayout {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineMedium
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .safeSingleClick {
                        onCreateTaskClicked()
                    }
                    .background(
                        color = ColorCian,
                        shape = CircleShape
                    )
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Open create task screen",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .safeSingleClick {
                        onAction(TasksScreenAction.CreateTestTask)
                    }
                    .background(
                        color = ColorCian,
                        shape = CircleShape
                    )
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create new test task",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.tasks,
                    key = { task -> task.id }
                ) { task ->
                    TaskItem(
                        task = task,
                        modifier = Modifier
                            .safeSingleClick {
                                onAction(TasksScreenAction.DeleteTask(task))
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TasksScreenPreview() {
    val tasks = mutableListOf<Task>()
    val lists = mutableListOf<String>()
    repeat(6) {
        tasks.add(
            Task(
                id = tasks.count(),
                name = "Some task name",
                startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 2,
                endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 5,
                list = "Test list ${tasks.count()}".also { lists.add(it) }
            )
        )
    }
    GridTaskTheme {
        TasksScreen(
            state = TasksScreenState(
                tasks = tasks,
                lists = lists
            ),
            onCreateTaskClicked = {},
            onAction = {}
        )
    }
}