package grid.task.nancymartin.presentation.features.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.modifiers.safeSingleClick
import grid.task.nancymartin.presentation.common.theme.ColorCian
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import grid.task.nancymartin.presentation.navigation.Screen

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
        }
    )
}

@Composable
private fun TasksScreen(
    state: TasksScreenState,
    onCreateTaskClicked: () -> Unit
) {
    MainScreensLayout {
        Box(
            modifier = Modifier
                .safeSingleClick {
                    onCreateTaskClicked()
                }
                .background(
                    color = ColorCian
                )
                .fillMaxSize()
        ) {

        }
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
private fun TasksScreenPreview() {
    GridTaskTheme {
        TasksScreen(
            state = TasksScreenState(),
            onCreateTaskClicked = {}
        )
    }
}