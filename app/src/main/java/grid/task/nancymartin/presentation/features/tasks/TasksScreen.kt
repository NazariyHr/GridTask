package grid.task.nancymartin.presentation.features.tasks

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import grid.task.nancymartin.R
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.components.ObserveAsEvent
import grid.task.nancymartin.presentation.common.components.SelectableTextField
import grid.task.nancymartin.presentation.common.modifiers.safeSingleClick
import grid.task.nancymartin.presentation.common.theme.ColorCian
import grid.task.nancymartin.presentation.common.theme.ColorDarkContainers
import grid.task.nancymartin.presentation.common.theme.ColorLightContainers
import grid.task.nancymartin.presentation.common.theme.ColorWhite
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import grid.task.nancymartin.presentation.common.theme.TextColorForHeadLinesAndDisplay
import grid.task.nancymartin.presentation.features.tasks.components.DaySelectorItem
import grid.task.nancymartin.presentation.features.tasks.components.GroupedTasksItem
import grid.task.nancymartin.presentation.features.tasks.components.TaskInfoDialog
import grid.task.nancymartin.presentation.features.tasks.components.calendar.TasksCalendar
import grid.task.nancymartin.presentation.features.tasks.ui_model.GroupedTasks
import grid.task.nancymartin.presentation.features.tasks.ui_model.TaskDisplayState
import grid.task.nancymartin.presentation.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun TasksScreenRoot(
    navController: NavController,
    viewModel: TasksViewModel =
        hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    TasksScreen(
        state = state,
        events = viewModel.events,
        onCreateTaskClicked = {
            navController.navigate(Screen.CreateTask)
        },
        onAction = viewModel::onAction,
        onPrivacyPolicyClicked = {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(ContextCompat.getString(context, R.string.privacy_policy))
            )
            context.startActivity(browserIntent)
        }
    )
}

@Composable
private fun TasksScreen(
    state: TasksScreenState,
    events: Flow<TasksScreenEvent>,
    onCreateTaskClicked: () -> Unit,
    onAction: (TasksScreenAction) -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    val d = LocalDensity.current
    val daysScrollState: LazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var clickedTask by remember {
        mutableStateOf<Task?>(null)
    }

    ObserveAsEvent(flow = events) { event ->
        when (event) {
            is TasksScreenEvent.ScrollToDay -> {
                scope.launch {
                    delay(300L)
                    if (event.withAnimation) {
                        daysScrollState.animateScrollToItem(
                            state.daysForCalendarSelector.indexOf(
                                event.day
                            )
                        )
                    } else {
                        daysScrollState.scrollToItem(state.daysForCalendarSelector.indexOf(event.day))
                    }
                }
            }
        }
    }

    MainScreensLayout {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                var filterListHeight by remember {
                    mutableStateOf(0.dp)
                }
                // Filtering list
                SelectableTextField(
                    selectedValueStr = state.filteringList,
                    label = "",
                    options = state.lists,
                    onValueChange = { newValue ->
                        onAction(TasksScreenAction.ChangeFilteringList(newValue))
                    },
                    optionsBackgroundColor = ColorDarkContainers,
                    optionContent = { option ->
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    unselectOption = stringResource(id = R.string.all_lists),
                    unselectOptionStr = stringResource(id = R.string.all_lists),
                    modifier = Modifier
                        .onPlaced {
                            filterListHeight = with(d) { it.size.height.toDp() }
                        }
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .height(filterListHeight)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_privacy_policy),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color(3, 53, 60, 255)),
                        modifier = Modifier
                            .safeSingleClick {
                                onPrivacyPolicyClicked()
                            }
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            // Display mode selector and add task button
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var addNewTaskHeight by remember {
                    mutableStateOf(0.dp)
                }

                // Mode selector
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            color = ColorLightContainers,
                            shape = CircleShape
                        )
                        .weight(1f)
                        .height(addNewTaskHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                onAction(TasksScreenAction.ChangeDisplayState(TaskDisplayState.LIST))
                            }
                            .background(
                                color = if (state.displayState == TaskDisplayState.LIST) ColorDarkContainers else Color.Transparent
                            )
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_list),
                            colorFilter = ColorFilter.tint(ColorWhite),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clickable {
                                onAction(TasksScreenAction.ChangeDisplayState(TaskDisplayState.CALENDAR))
                            }
                            .background(
                                color = if (state.displayState == TaskDisplayState.CALENDAR) ColorDarkContainers else Color.Transparent
                            )
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            colorFilter = ColorFilter.tint(ColorWhite),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                        )
                    }
                }

                // Add new task button
                Box(
                    modifier = Modifier
                        .onPlaced {
                            addNewTaskHeight = with(d) { it.size.height.toDp() }
                        }
                        .safeSingleClick {
                            onCreateTaskClicked()
                        }
                        .background(
                            color = ColorCian,
                            shape = CircleShape
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .weight(1f),
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
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.add_new_task),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            // Tasks
            when (state.displayState) {
                TaskDisplayState.LIST -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(
                            items = state.groupedTasks
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

                TaskDisplayState.CALENDAR -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Calendar view
                        TasksCalendar(
                            tasks = state.tasks,
                            selectedDay = state.selectedDayForCalendar,
                            onTaskClicked = { task ->
                                clickedTask = task
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                        )

                        // Day selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            var daysHeight by remember {
                                mutableStateOf(0.dp)
                            }

                            LazyRow(
                                state = daysScrollState,
                                modifier = Modifier
                                    .onPlaced {
                                        daysHeight = with(d) { it.size.height.toDp() }
                                    }
                                    .weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(
                                    items = state.daysForCalendarSelector
                                ) { day ->
                                    DaySelectorItem(
                                        dateTime = day,
                                        isSelected = day == state.selectedDayForCalendar,
                                        onDaySelected = { newSelectedDay ->
                                            onAction(
                                                TasksScreenAction.ChangeSelectedDay(
                                                    newSelectedDay
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        onAction(TasksScreenAction.SetCurrentDaySelected)
                                    }
                                    .align(Alignment.CenterVertically)
                                    .background(
                                        color = ColorLightContainers,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .height(daysHeight)
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.set_today),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (clickedTask != null) {
            TaskInfoDialog(
                task = clickedTask!!,
                onDismiss = {
                    clickedTask = null
                },
                onDeleteClicked = { task ->
                    onAction(TasksScreenAction.DeleteTask(task))
                    clickedTask = null
                },
                onDoneChange = { task, isDone ->
                    onAction(TasksScreenAction.ChangeTaskIsDone(task, isDone))
                }
            )
        }
    }
}

@Preview
@Composable
private fun TasksScreenListDisplayPreview() {
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
                title = R.string.today,
                tasks = tasks
            )
        )
    }
    GridTaskTheme {
        TasksScreen(
            state = TasksScreenState(
                groupedTasks = groupedTasks,
                lists = lists,
                filteringList = "List 1",
                displayState = TaskDisplayState.LIST
            ),
            events = flowOf(),
            onCreateTaskClicked = {},
            onAction = {},
            onPrivacyPolicyClicked = {}
        )
    }
}

@Preview
@Composable
private fun TasksScreenCalendarDisplayPreview() {
    val lists = mutableListOf<String>()
    val tasks = mutableListOf<Task>()
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 1,
            list = "Test list ${tasks.count()}".also { lists.add(it) },
            done = false
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 1,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}".also { lists.add(it) },
            done = false
        )
    )

    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val todayMillis = today.timeInMillis
    val dayAfterWeekMillis = today.apply { add(Calendar.DAY_OF_YEAR, 2) }.timeInMillis

    val days = run {
        today.add(Calendar.DAY_OF_YEAR, -4)
        val days = mutableListOf<Long>()
        while (today.timeInMillis < dayAfterWeekMillis) {
            today.add(Calendar.DAY_OF_YEAR, 1)
            days.add(today.timeInMillis)
        }
        days
    }
    GridTaskTheme {
        TasksScreen(
            state = TasksScreenState(
                tasks = tasks,
                lists = lists,
                filteringList = "List 1",
                displayState = TaskDisplayState.CALENDAR,
                selectedDayForCalendar = todayMillis,
                daysForCalendarSelector = days
            ),
            events = flowOf(),
            onCreateTaskClicked = {},
            onAction = {},
            onPrivacyPolicyClicked = {}
        )
    }
}