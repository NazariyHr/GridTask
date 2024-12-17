package grid.task.nancymartin.presentation.features.tasks.components.calendar

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.utils.getSecondsOfThisDay
import grid.task.nancymartin.domain.utils.getToday
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.theme.ColorCian
import grid.task.nancymartin.presentation.common.theme.ColorCianDarker
import grid.task.nancymartin.presentation.common.theme.ColorGreenLight
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by nazar at 15.08.2024
 */
@Composable
fun TasksCalendar(
    tasks: List<Task>,
    selectedDay: Long,
    onTaskClicked: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val d = LocalDensity.current

    var currTime by remember {
        mutableLongStateOf(Calendar.getInstance().timeInMillis)
    }
    val secondsInCurrentTimeAtThisDay by remember(currTime) {
        derivedStateOf {
            currTime.getSecondsOfThisDay()
        }
    }
    val isCurrentDaySelected by remember(selectedDay) {
        derivedStateOf {
            selectedDay == getToday().timeInMillis
        }
    }

    LaunchedEffect(Unit) {
        currTime = Calendar.getInstance().timeInMillis
        scope.launch {
            while (true) {
                delay(1000L)
                currTime = Calendar.getInstance().timeInMillis
            }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val scaleValueTextStyle = MaterialTheme.typography.labelMedium
    val taskTitleTextStyle = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Bold
    )
    val taskDescriptionTextStyle = MaterialTheme.typography.labelMedium.copy(
        fontStyle = FontStyle.Italic
    )

    var drawWidth by remember {
        mutableFloatStateOf(0f)
    }
    var drawHeight by remember {
        mutableFloatStateOf(0f)
    }

    val hours = run {
        val hours = mutableListOf<Int>()
        repeat(25) {
            hours.add(hours.count())
        }
        hours
    }
    val hoursInScaleToDraw = hours.map {
        (if (it < 10) "0" else "") + "$it:00"
    }
    val hoursInScaleToDrawLayoutResults by remember(hoursInScaleToDraw, scaleValueTextStyle) {
        derivedStateOf {
            hoursInScaleToDraw.map {
                textMeasurer.measure(it, scaleValueTextStyle)
            }
        }
    }
    val maxTextWidth by remember(hoursInScaleToDrawLayoutResults) {
        derivedStateOf {
            hoursInScaleToDrawLayoutResults.maxOf { it.size.width }
        }
    }
    val textHeight by remember(hoursInScaleToDrawLayoutResults) {
        derivedStateOf {
            hoursInScaleToDrawLayoutResults.maxOf { it.size.height }
        }
    }
    val heightPxInOneSecond by remember(drawHeight) {
        derivedStateOf {
            (drawHeight - textHeight.toFloat()) / (hours.count() - 1).toFloat() / 60f / 60f
        }
    }

    val currTimeToDraw by remember(currTime) {
        derivedStateOf {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(Date(currTime))
        }
    }
    val currTimeToDrawLayoutResult by remember(currTimeToDraw, scaleValueTextStyle) {
        derivedStateOf {
            textMeasurer.measure(currTimeToDraw, scaleValueTextStyle)
        }
    }

    val indicatorColor = Color(241, 101, 91, 255)

    val selectedDayTasks by remember(tasks, selectedDay) {
        derivedStateOf {
            tasks.filter { task ->
                val startTimeC = Calendar.getInstance().apply { timeInMillis = task.startTime }
                val endTimeC = Calendar.getInstance().apply { timeInMillis = task.endTime }
                val selectedDayC = Calendar.getInstance().apply { timeInMillis = selectedDay }

                (startTimeC.get(Calendar.YEAR) == selectedDayC.get(Calendar.YEAR) &&
                        startTimeC.get(Calendar.MONTH) == selectedDayC.get(Calendar.MONTH) &&
                        startTimeC.get(Calendar.DAY_OF_MONTH) == selectedDayC.get(Calendar.DAY_OF_MONTH))
                        ||
                        (endTimeC.get(Calendar.YEAR) == selectedDayC.get(Calendar.YEAR) &&
                                endTimeC.get(Calendar.MONTH) == selectedDayC.get(Calendar.MONTH) &&
                                endTimeC.get(Calendar.DAY_OF_MONTH) == selectedDayC.get(Calendar.DAY_OF_MONTH))
                        ||
                        selectedDay in task.startTime..task.endTime
            }
        }
    }
    val horizontalPaddingForScale = with(d) { 4.dp.toPx() }
    val taskContainerOutPadding = with(d) { 2.dp.toPx() }
    val taskContainerInnerPadding = with(d) { 4.dp.toPx() }
    val paddingBetweenTitleAndDescription = with(d) { 4.dp.toPx() }

    val groupedTasksToDrawState by remember(
        selectedDay,
        selectedDayTasks,
        heightPxInOneSecond,
        textHeight
    ) {
        derivedStateOf<Map<Int, List<TaskDrawInfo>>> {
            val tasksToDraw = mutableListOf<TaskDrawInfo>()
            val groupedTasksToDraw = mutableMapOf<Int, List<TaskDrawInfo>>()

            if (selectedDayTasks.isNotEmpty()) {
                selectedDayTasks.forEach { task ->
                    val endOfSelectedDay = Calendar
                        .getInstance()
                        .apply {
                            timeInMillis = selectedDay
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                            set(Calendar.MILLISECOND, 999)
                        }.timeInMillis

                    val startOfTaskForToday = if (task.startTime < selectedDay) {
                        selectedDay
                    } else task.startTime
                    val endOfTaskForToday = if (task.endTime > endOfSelectedDay) {
                        endOfSelectedDay
                    } else task.endTime

                    val startSeconds = startOfTaskForToday.getSecondsOfThisDay()
                    val endSeconds = endOfTaskForToday.getSecondsOfThisDay()

                    val yStartOfTask =
                        startSeconds * heightPxInOneSecond + textHeight.toFloat() / 2
                    val yEndOfTask =
                        endSeconds * heightPxInOneSecond + textHeight.toFloat() / 2

                    tasksToDraw.add(
                        TaskDrawInfo(
                            task = task,
                            yStartOfTask = yStartOfTask,
                            yEndOfTask = yEndOfTask
                        )
                    )
                }

                tasksToDraw.sortByDescending { it.yEndOfTask - it.yStartOfTask }

                groupedTasksToDraw[0] = listOf(tasksToDraw.first())

                tasksToDraw
                    .drop(1)
                    .forEach { taskToDraw ->
                        var groupKeyForTask = -1
                        groupedTasksToDraw.keys.forEach searchKey@{ key ->
                            groupedTasksToDraw[key]!!.forEach { taskInGroup ->
                                if (
                                    taskToDraw.yStartOfTask in taskInGroup.yStartOfTask..taskInGroup.yEndOfTask ||
                                    taskToDraw.yEndOfTask in taskInGroup.yStartOfTask..taskInGroup.yEndOfTask ||
                                    taskInGroup.yStartOfTask in taskToDraw.yStartOfTask..taskToDraw.yEndOfTask ||
                                    taskInGroup.yEndOfTask in taskToDraw.yStartOfTask..taskToDraw.yEndOfTask
                                ) {
                                    groupKeyForTask = key
                                    return@searchKey
                                }
                            }
                        }

                        if (groupKeyForTask == -1) {
                            groupedTasksToDraw[groupedTasksToDraw.keys.count()] =
                                groupedTasksToDraw[groupedTasksToDraw.keys.count()].orEmpty() + taskToDraw
                        } else {
                            groupedTasksToDraw[groupKeyForTask] =
                                groupedTasksToDraw[groupKeyForTask].orEmpty() + taskToDraw
                        }
                    }
            }

            groupedTasksToDraw
        }
    }

    var pressedPoint by remember {
        mutableStateOf(Offset.Zero)
    }

    LaunchedEffect(pressedPoint) {
        scope.launch {
            if (pressedPoint != Offset.Zero) {
                val scaleXInset = maxTextWidth.toFloat() + horizontalPaddingForScale * 2

                groupedTasksToDrawState.values.forEach searchTask@{ tasksGroup ->
                    val tasksDrawWidth =
                        drawWidth - (maxTextWidth.toFloat() + horizontalPaddingForScale * 2)
                    val taskDrawWidth = tasksDrawWidth / tasksGroup.count()

                    tasksGroup
                        .sortedBy { it.task.startTime }
                        .forEachIndexed { index, taskToDraw ->
                            val yStart = taskToDraw.yStartOfTask
                            val xStart = ((taskDrawWidth + taskContainerOutPadding) * index)
                            val yEnd =
                                yStart + (taskToDraw.yEndOfTask - taskToDraw.yStartOfTask)
                            val xEnd = (xStart + (taskDrawWidth - taskContainerOutPadding))

                            val clickCoordinateX = pressedPoint.x - scaleXInset
                            val clickCoordinateY = pressedPoint.y
                            if (clickCoordinateX in xStart..xEnd && clickCoordinateY in yStart..yEnd) {
                                // User clicked on this task
                                onTaskClicked(taskToDraw.task)
                                return@searchTask
                            }
                        }
                }
            }
        }
    }

    Spacer(
        modifier = modifier
            .pointerInput(true) {
                detectTapGestures(onTap = { offset ->
                    pressedPoint = offset
                })
            }
            .drawBehind {
                drawWidth = size.width
                drawHeight = size.height

                for (i in 0..<hoursInScaleToDraw.count()) {
                    val y = Calendar
                        .getInstance()
                        .apply {
                            if (i == 24) {
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                                set(Calendar.SECOND, 59)
                                set(Calendar.MILLISECOND, 999)
                            } else {
                                set(Calendar.HOUR_OF_DAY, i)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                        }
                        .let {
                            (it.get(Calendar.HOUR_OF_DAY) * 60 * 60) +
                                    (it.get(Calendar.MINUTE) * 60) +
                                    it.get(Calendar.SECOND)
                        } * heightPxInOneSecond

                    drawText(
                        textMeasurer = textMeasurer,
                        text = hoursInScaleToDraw[i],
                        style = scaleValueTextStyle,
                        topLeft = Offset(
                            x = horizontalPaddingForScale,
                            y = y - textHeight / 2 + textHeight.toFloat() / 2
                        )
                    )

                    drawLine(
                        color = ColorCian,
                        start = Offset(
                            x = maxTextWidth.toFloat() + horizontalPaddingForScale * 2,
                            y = y + textHeight.toFloat() / 2
                        ),
                        end = Offset(
                            x = size.width,
                            y = y + textHeight.toFloat() / 2
                        ),
                        strokeWidth = 1.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                if (isCurrentDaySelected) {
                    val currTimeToDrawY = secondsInCurrentTimeAtThisDay * heightPxInOneSecond
                    drawRoundRect(
                        color = indicatorColor,
                        topLeft = Offset(
                            x = 0f,
                            y = currTimeToDrawY - currTimeToDrawLayoutResult.size.height / 2 + textHeight.toFloat() / 2
                        ),
                        size = Size(
                            width = currTimeToDrawLayoutResult.size.width.toFloat() + horizontalPaddingForScale * 2,
                            height = currTimeToDrawLayoutResult.size.height.toFloat()
                        ),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = currTimeToDraw,
                        style = scaleValueTextStyle,
                        topLeft = Offset(
                            x = horizontalPaddingForScale,
                            y = currTimeToDrawY - currTimeToDrawLayoutResult.size.height / 2 + textHeight.toFloat() / 2
                        )
                    )
                }

                inset(
                    left = maxTextWidth.toFloat() + horizontalPaddingForScale * 2,
                    top = 0f,
                    right = 0f,
                    bottom = 0f
                ) {
                    groupedTasksToDrawState.values.forEach { tasksGroup ->
                        val taskDrawWidth = size.width / tasksGroup.count()
                        tasksGroup
                            .sortedBy { it.task.startTime }
                            .forEachIndexed { index, taskToDraw ->
                                val x = (taskDrawWidth + taskContainerOutPadding) * index
                                val y = taskToDraw.yStartOfTask

                                drawRoundRect(
                                    color = if (taskToDraw.task.done) ColorGreenLight else ColorCianDarker,
                                    topLeft = Offset(
                                        x = x,
                                        y = y
                                    ),
                                    size = Size(
                                        width = taskDrawWidth - taskContainerOutPadding,
                                        height = taskToDraw.yEndOfTask - taskToDraw.yStartOfTask
                                    ),
                                    cornerRadius = CornerRadius(4.dp.toPx())
                                )

                                val availableHeightForText =
                                    taskToDraw.yEndOfTask - taskToDraw.yStartOfTask - taskContainerInnerPadding * 2
                                val availableWidthForText =
                                    taskDrawWidth - taskContainerOutPadding - taskContainerInnerPadding

                                if (availableHeightForText > 0 && availableWidthForText > 0) {
                                    val titleLayoutResult =
                                        textMeasurer.measure(
                                            taskToDraw.task.title,
                                            taskTitleTextStyle,
                                            constraints = Constraints(
                                                maxWidth = availableWidthForText.toInt(),
                                                maxHeight = availableHeightForText.toInt()
                                            )
                                        )

                                    drawText(
                                        textMeasurer = textMeasurer,
                                        text = taskToDraw.task.title,
                                        style = taskTitleTextStyle,
                                        topLeft = Offset(
                                            x = (taskDrawWidth + taskContainerOutPadding) * index + taskContainerInnerPadding,
                                            y = taskToDraw.yStartOfTask + taskContainerInnerPadding
                                        ),
                                        size = Size(
                                            width = taskDrawWidth - taskContainerOutPadding - taskContainerInnerPadding,
                                            height = availableHeightForText
                                        )
                                    )
                                    if (availableHeightForText > (titleLayoutResult.size.height + paddingBetweenTitleAndDescription)) {
                                        drawText(
                                            textMeasurer = textMeasurer,
                                            text = taskToDraw.task.description,
                                            style = taskDescriptionTextStyle,
                                            topLeft = Offset(
                                                x = (taskDrawWidth + taskContainerOutPadding) * index + taskContainerInnerPadding,
                                                y = taskToDraw.yStartOfTask + taskContainerInnerPadding + titleLayoutResult.size.height + paddingBetweenTitleAndDescription
                                            ),
                                            overflow = TextOverflow.Ellipsis,
                                            size = Size(
                                                width = taskDrawWidth - taskContainerOutPadding - taskContainerInnerPadding,
                                                height = availableHeightForText - (titleLayoutResult.size.height + paddingBetweenTitleAndDescription) - taskContainerInnerPadding
                                            )
                                        )
                                    }
                                }
                            }
                    }

                    if (isCurrentDaySelected) {
                        val y =
                            secondsInCurrentTimeAtThisDay * heightPxInOneSecond + textHeight.toFloat() / 2
                        drawLine(
                            color = indicatorColor,
                            start = Offset(
                                x = 0f,
                                y = y
                            ),
                            end = Offset(
                                x = size.width,
                                y = y
                            ),
                            strokeWidth = 1.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
            .fillMaxSize()
    )
}

@Preview
@Composable
private fun TasksCalendarPreview() {
    val tasks = mutableListOf<Task>()
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}",
            done = false
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 1,
            list = "Test list ${tasks.count()}",
            done = true
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 1,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}",
            done = false
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}",
            done = false
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some task title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}",
            done = true
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some very long long title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 3,
            list = "Test list ${tasks.count()}",
            done = false
        )
    )
    tasks.add(
        Task(
            id = tasks.count(),
            title = "Some very long long title",
            description = "Some task description",
            startTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 3,
            endTime = Calendar.getInstance().timeInMillis - 1000 * 60 * 60 * 1,
            list = "Test list ${tasks.count()}",
            done = false
        )
    )

    val today = getToday().timeInMillis

    GridTaskTheme {
        MainScreensLayout {
            TasksCalendar(
                tasks = tasks,
                selectedDay = today,
                onTaskClicked = { },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}