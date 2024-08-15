package grid.task.nancymartin.presentation.features.tasks.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.theme.ColorCian
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
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val d = LocalDensity.current

    var currTime by remember {
        mutableLongStateOf(Calendar.getInstance().timeInMillis)
    }
    val secondsInCurrentTimeAtThisDay by remember(currTime) {
        derivedStateOf {
            Calendar
                .getInstance()
                .apply { timeInMillis = currTime }
                .let {
                    (it.get(Calendar.HOUR_OF_DAY) * 60 * 60) +
                            (it.get(Calendar.MINUTE) * 60) +
                            it.get(Calendar.SECOND)
                }
        }
    }
    val isCurrentDaySelected by remember(selectedDay) {
        derivedStateOf {
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            selectedDay == today.timeInMillis
        }
    }

    LaunchedEffect(Unit) {
        currTime = Calendar.getInstance().timeInMillis
        scope.launch {
            while (true) {
                delay(1000L)
                //currTime += 1000L * 60 * 5
                currTime = Calendar.getInstance().timeInMillis
            }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val scaleValueTextStyle = MaterialTheme.typography.labelMedium

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

    val selectedDayTasks by remember(tasks) {
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
            }
        }
    }

    Box(
        modifier = modifier
            .drawBehind {
                drawWidth = size.width
                drawHeight = size.height
                val horizontalPaddingForScale = 4.dp.toPx()

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

//                drawOutline(
//                    outline = Outline.Rounded(
//                        RoundRect(
//                            left = 0f,
//                            top = 0f,
//                            right = size.width,
//                            bottom = size.height
//                        )
//                    ),
//                    color = ColorCianDark,
//                    style = Stroke(1.dp.toPx())
//                )

//                drawOutline(
//                    outline = Outline.Rounded(
//                        RoundRect(
//                            left = 0f,
//                            top = textHeight.toFloat() / 2f,
//                            right = size.width,
//                            bottom = size.height - textHeight.toFloat() / 2f
//                        )
//                    ),
//                    color = Color(32, 73, 34, 255),
//                    style = Stroke(1.dp.toPx())
//                )

                inset(
                    left = maxTextWidth.toFloat() + horizontalPaddingForScale * 2,
                    top = 0f,
                    right = 0f,
                    bottom = 0f
                ) {
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
    ) {

    }
}

@Preview
@Composable
private fun TasksCalendarPreview() {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    GridTaskTheme {
        MainScreensLayout {
            TasksCalendar(
                tasks = listOf(),
                selectedDay = today,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}