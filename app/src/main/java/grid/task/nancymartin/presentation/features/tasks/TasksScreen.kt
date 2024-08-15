package grid.task.nancymartin.presentation.features.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
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
import grid.task.nancymartin.presentation.common.theme.TextColorForHeadLinesAndDisplay
import grid.task.nancymartin.presentation.features.tasks.components.GroupedTasksItem
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
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
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Spacer(
                                modifier = Modifier
                                    .drawBehind {
                                        val linesLength = size.height / 13f * 8f
                                        inset(1.dp.toPx()) {
                                            drawLine(
                                                color = TextColorForHeadLinesAndDisplay,
                                                start = Offset(
                                                    x = center.x - linesLength / 2,
                                                    y = center.y,
                                                ),
                                                end = Offset(
                                                    x = center.x + linesLength / 2,
                                                    y = center.y,
                                                ),
                                                strokeWidth = 2.dp.toPx(),
                                                cap = StrokeCap.Round
                                            )

                                            drawLine(
                                                color = TextColorForHeadLinesAndDisplay,
                                                start = Offset(
                                                    x = center.x,
                                                    y = center.y - linesLength / 2,
                                                ),
                                                end = Offset(
                                                    x = center.x,
                                                    y = center.y + linesLength / 2,
                                                ),
                                                strokeWidth = 2.dp.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        }
                                    }
                                    .align(Alignment.CenterVertically)
                                    .size(32.dp)
                            )
                            Text(
                                text = "Add new task",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .safeSingleClick {
                                onAction(TasksScreenAction.CreateTestTask)
                            }
                            .background(
                                color = ColorCian,
                                shape = CircleShape
                            )
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Create new test task",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                itemsIndexed(
                    items = state.groupedTasks,
                    key = { _, groupedTask -> groupedTask.title }
                ) { index, groupedTask ->
                    val gap = 20.dp
                    GroupedTasksItem(
                        groupedTask = groupedTask,
                        onDoneChange = { task, isDone ->
                            onAction(TasksScreenAction.ChangeTaskIsDone(task, isDone))
                        },
                        onDeleteClicked = { task ->
                            onAction(TasksScreenAction.DeleteTask(task))
                        },
                        modifier = Modifier
                            .padding(
                                top = if (index == 0) 0.dp else gap / 2,
                                bottom = if (index == state.groupedTasks.count() - 1) 0.dp else gap / 2
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TasksScreenPreview() {
    val groupedTasks = mutableListOf<GroupedTasks>()
    val lists = mutableListOf<String>()
    repeat(2) {
        val tasks = mutableListOf<Task>()
        repeat((groupedTasks.count() + 1)) {
            tasks.add(
                Task(
                    id = tasks.count(),
                    title = "Some task title",
                    description = "Some task description",
                    startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 2,
                    endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * tasks.count() + 5,
                    list = "Test list ${tasks.count()}".also { lists.add(it) },
                    done = false
                )
            )
        }
        groupedTasks.add(
            GroupedTasks(
                title = "day ${groupedTasks.count()}",
                tasks = tasks
            )
        )
    }
    GridTaskTheme {
        TasksScreen(
            state = TasksScreenState(
                groupedTasks = groupedTasks,
                lists = lists
            ),
            onCreateTaskClicked = {},
            onAction = {}
        )
    }
}