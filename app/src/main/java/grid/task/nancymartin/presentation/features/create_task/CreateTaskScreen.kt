package grid.task.nancymartin.presentation.features.create_task

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme

@Composable
fun CreateTaskScreenRoot(
    navController: NavController,
    viewModel: CreateTaskViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateTaskScreen(
        state = state
    )
}

@Composable
private fun CreateTaskScreen(
    state: CreateTaskScreenState
) {
    MainScreensLayout {
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
private fun CreateTaskScreenPreview() {
    GridTaskTheme {
        CreateTaskScreen(
            state = CreateTaskScreenState()
        )
    }
}