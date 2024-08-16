package grid.task.nancymartin.presentation.features.tasks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import grid.task.nancymartin.R
import grid.task.nancymartin.domain.model.Task
import grid.task.nancymartin.domain.utils.toFormattedDateAndTime
import grid.task.nancymartin.presentation.common.modifiers.safeSingleClick
import grid.task.nancymartin.presentation.common.theme.ColorLightContainers
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import java.util.Calendar

/**
 * Created by nazar at 16.08.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoDialog(
    task: Task,
    onDismiss: () -> Unit,
    onDeleteClicked: (Task) -> Unit,
    onDoneChange: (Task, Boolean) -> Unit
) {
    var taskIsDone by remember {
        mutableStateOf(task.done)
    }
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorLightContainers,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                val startTimeStr by remember {
                    derivedStateOf {
                        task.startTime.toFormattedDateAndTime()
                    }
                }
                val endTimeStr by remember {
                    derivedStateOf {
                        task.endTime.toFormattedDateAndTime()
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            Checkbox(
                                checked = taskIsDone,
                                onCheckedChange = {
                                    taskIsDone = it
                                    onDoneChange(task, it)
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.Gray),
                            modifier = Modifier
                                .safeSingleClick {
                                    onDismiss()
                                }
                                .align(Alignment.CenterEnd)
                                .size(28.dp)
                        )
                    }
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        Text(
                            text = "Start at: ",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = startTimeStr,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                    Row {
                        Text(
                            text = "End at: ",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = endTimeStr,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                    if (task.list.isNotEmpty()) {
                        Text(
                            text = task.list,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .safeSingleClick {
                                onDeleteClicked(task)
                            }
                            .background(
                                color = Color.Red,
                                shape = CircleShape
                            )
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Remove",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun TaskInfoDialogPreview() {
    GridTaskTheme {
        TaskInfoDialog(
            task = Task(
                id = 0,
                title = "Some task title",
                description = "Some task description",
                startTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 2,
                endTime = Calendar.getInstance().timeInMillis + 1000 * 60 * 5,
                list = "Test list 1",
                done = true
            ),
            onDismiss = {},
            onDeleteClicked = {},
            onDoneChange = { _, _ -> }
        )
    }
}