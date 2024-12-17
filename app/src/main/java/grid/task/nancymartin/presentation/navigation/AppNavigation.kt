package grid.task.nancymartin.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import grid.task.nancymartin.presentation.features.create_task.CreateTaskScreenRoot
import grid.task.nancymartin.presentation.features.tasks.TasksScreenRoot

@Composable
fun AppNavigationRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AppNavigation(
        navController,
        modifier
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var openedScreens by rememberSaveable {
        mutableStateOf(listOf<Screen>())
    }
    Column(
        modifier = modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks,
            modifier = Modifier.weight(1f)
        ) {
            composable<Screen.Tasks>(
                enterTransition = {
                    if (!openedScreens.any { it is Screen.CreateTask })
                        slideIn(
                            initialOffset = { fullSize ->
                                IntOffset(0, fullSize.height)
                            }
                        )
                    else fadeIn()
                },
                exitTransition = {
                    fadeOut()
                }
            ) {
                TasksScreenRoot(navController)
                DisposableEffect(true) {
                    openedScreens = openedScreens + Screen.Tasks
                    onDispose {
                        openedScreens = openedScreens.filterNot { it is Screen.Tasks }
                    }
                }
            }
            composable<Screen.CreateTask>(
                enterTransition = {
                    slideIn(
                        initialOffset = { fullSize ->
                            IntOffset(0, fullSize.height)
                        }
                    )
                },
                exitTransition = {
                    slideOut(
                        targetOffset = { fullSize ->
                            IntOffset(0, fullSize.height + fullSize.height / 4)
                        }
                    )
                }
            ) {
                CreateTaskScreenRoot(navController)
                DisposableEffect(true) {
                    openedScreens = openedScreens + Screen.CreateTask
                    onDispose {
                        openedScreens = openedScreens.filterNot { it is Screen.CreateTask }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppNavigationPreview(modifier: Modifier = Modifier) {
    GridTaskTheme {
        AppNavigation(
            navController = rememberNavController()
        )
    }
}