@file:OptIn(ExperimentalMaterial3Api::class)

package grid.task.nancymartin.presentation.features.create_task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import grid.task.nancymartin.R
import grid.task.nancymartin.domain.utils.convertMillisToDate
import grid.task.nancymartin.presentation.common.components.DatePickerModal
import grid.task.nancymartin.presentation.common.components.MainScreensLayout
import grid.task.nancymartin.presentation.common.components.TimePickerDialogDial
import grid.task.nancymartin.presentation.common.components.keyboardIsOpened
import grid.task.nancymartin.presentation.common.modifiers.safeSingleClick
import grid.task.nancymartin.presentation.common.theme.ColorCianDark
import grid.task.nancymartin.presentation.common.theme.ColorCianLight
import grid.task.nancymartin.presentation.common.theme.ColorDarkContainers
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import java.util.Calendar

@Composable
fun CreateTaskScreenRoot(
    navController: NavController,
    viewModel: CreateTaskViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateTaskScreen(
        state = state,
        onAction = viewModel::onAction,
        navigateUp = {
            navController.navigateUp()
        }
    )
}

typealias Hours = Int
typealias Minutes = Int

@Composable
private fun CreateTaskScreen(
    state: CreateTaskScreenState,
    onAction: (CreateTaskScreenAction) -> Unit,
    navigateUp: () -> Unit
) {
    val d = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val keyboardIsOpened by keyboardIsOpened()

    LaunchedEffect(keyboardIsOpened) {
        if (!keyboardIsOpened) {
            focusManager.clearFocus()
        }
    }

    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var startDate by rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    val startDateStr by remember {
        derivedStateOf {
            startDate?.let { convertMillisToDate(it) }.orEmpty()
        }
    }
    var startTime by rememberSaveable {
        mutableStateOf<Pair<Hours, Minutes>?>(null)
    }
    val startTimeStr by remember {
        derivedStateOf {
            startTime?.let {
                (if (it.first < 10) "0" else "") + "${it.first}" +
                        ":" +
                        (if (it.second < 10) "0" else "") + "${it.second}"
            }.orEmpty()
        }
    }
    var showStartDateDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showStartTimeDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var endDate by rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    val endDateStr by remember {
        derivedStateOf {
            endDate?.let { convertMillisToDate(it) }.orEmpty()
        }
    }
    var endTime by rememberSaveable {
        mutableStateOf<Pair<Hours, Minutes>?>(null)
    }
    val endTimeStr by remember {
        derivedStateOf {
            endTime?.let {
                (if (it.first < 10) "0" else "") + "${it.first}" +
                        ":" +
                        (if (it.second < 10) "0" else "") + "${it.second}"
            }.orEmpty()
        }
    }
    var showEndDateDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showEndTimeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val selectedStartDateAndTime by remember {
        derivedStateOf {
            if (startDate == null || startTime == null) null
            else Calendar
                .getInstance()
                .apply {
                    timeInMillis = startDate!!
                    set(Calendar.HOUR_OF_DAY, startTime!!.first)
                    set(Calendar.MINUTE, startTime!!.second)
                }
                .timeInMillis
        }
    }
    val selectedEndDateAndTime by remember {
        derivedStateOf {
            if (endDate == null || endTime == null) null
            else Calendar
                .getInstance()
                .apply {
                    timeInMillis = endDate!!
                    set(Calendar.HOUR_OF_DAY, endTime!!.first)
                    set(Calendar.MINUTE, endTime!!.second)
                }
                .timeInMillis
        }
    }

    var list by remember {
        mutableStateOf("")
    }
    val listsToShow by remember(state.lists) {
        derivedStateOf {
            if (list.isEmpty()) {
                state.lists
            } else {
                state.lists.filter {
                    it.lowercase().split(" ").any { list.lowercase().split(" ").contains(it) }
                }
            }
        }
    }
    var showListsPopUp by rememberSaveable {
        mutableStateOf(false)
    }
    var listsOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var listsPopupHeight by remember {
        mutableIntStateOf(0)
    }
    var statusBarHeight by remember {
        mutableStateOf(0.dp)
    }
    val listsMaxHeight by remember(listsOffset) {
        derivedStateOf {
            with(d) { listsOffset.y.toDp() - statusBarHeight }
        }
    }

    var titleError by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionError by rememberSaveable {
        mutableStateOf("")
    }
    var timeError by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold { paddingValues ->
        statusBarHeight = paddingValues.calculateTopPadding()
    }

    MainScreensLayout {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Add new task",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id =  R.drawable.ic_close),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = ColorCianDark),
                    modifier = Modifier
                        .safeSingleClick {
                            navigateUp()
                        }
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                )
            }

            OutlinedTextField(
                value = title,
                supportingText = if (titleError.isNotEmpty()) {
                    {
                        Text(
                            text = titleError,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else null,
                isError = titleError.isNotEmpty(),
                onValueChange = { newText ->
                    title = newText
                    titleError = ""
                },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 2,
                label = {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors()
            )

            OutlinedTextField(
                value = description,
                supportingText = if (descriptionError.isNotEmpty()) {
                    {
                        Text(
                            text = descriptionError,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else null,
                isError = descriptionError.isNotEmpty(),
                onValueChange = { newText ->
                    description = newText
                    descriptionError = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                maxLines = 10,
                label = {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = startDateStr,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickable {
                            showStartDateDialog = !showStartDateDialog
                        },
                    maxLines = 10,
                    label = {
                        Text(
                            text = "Start date",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledIndicatorColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor
                    )
                )
                OutlinedTextField(
                    value = startTimeStr,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickable {
                            showStartTimeDialog = !showStartTimeDialog
                        },
                    maxLines = 10,
                    label = {
                        Text(
                            text = "Start time",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledIndicatorColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = endDateStr,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickable {
                            showEndDateDialog = !showEndDateDialog
                        },
                    maxLines = 10,
                    label = {
                        Text(
                            text = "End date",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledIndicatorColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor
                    )
                )
                OutlinedTextField(
                    value = endTimeStr,
                    onValueChange = { },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickable {
                            showEndTimeDialog = !showEndTimeDialog
                        },
                    maxLines = 10,
                    label = {
                        Text(
                            text = "End time",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        disabledIndicatorColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor
                    )
                )
            }

            if (timeError.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = timeError,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            OutlinedTextField(
                value = list,
                onValueChange = { newText ->
                    list = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .onGloballyPositioned {
                        listsOffset = it
                            .localToRoot(
                                Offset(
                                    0f,
                                    0f
                                )
                            )
                    }
                    .onFocusChanged { focusState ->
                        showListsPopUp = focusState.isFocused
                    },
                maxLines = 10,
                label = {
                    Text(
                        text = "List",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors()
            )

            Button(
                onClick = {
                    var validationPassed = true
                    if (title.isEmpty()) {
                        titleError = "Please enter some title"
                        validationPassed = false
                    }
                    if (description.isEmpty()) {
                        descriptionError = "Please enter description"
                        validationPassed = false
                    }
                    if (selectedStartDateAndTime == null && selectedEndDateAndTime == null) {
                        timeError = "Please enter start and end date and time"
                        validationPassed = false
                    } else if (selectedStartDateAndTime == null || selectedEndDateAndTime == null) {
                        if (selectedStartDateAndTime == null) {
                            timeError = "Please enter start date and time"
                            validationPassed = false
                        }
                        if (selectedEndDateAndTime == null) {
                            timeError = "Please enter end date and time"
                            validationPassed = false
                        }
                    } else {
                        if (selectedStartDateAndTime!! >= selectedEndDateAndTime!!) {
                            timeError = "Start date and time must be before end date and time"
                            validationPassed = false
                        }
                    }

                    if (validationPassed) {
                        onAction(
                            CreateTaskScreenAction.CreateTestTask(
                                title = title,
                                description = description,
                                startTime = selectedStartDateAndTime!!,
                                endTime = selectedEndDateAndTime!!,
                                list = list
                            )
                        )
                        navigateUp()
                    }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (showStartDateDialog) {
            DatePickerModal(
                title = "Start date",
                onDateSelected = { selectedDateMilliseconds ->
                    startDate = selectedDateMilliseconds
                    timeError = ""
                },
                onDismiss = {
                    showStartDateDialog = false
                }
            )
        }

        if (showEndDateDialog) {
            DatePickerModal(
                title = "End date",
                onDateSelected = { selectedDateMilliseconds ->
                    endDate = selectedDateMilliseconds
                    timeError = ""
                },
                onDismiss = {
                    showEndDateDialog = false
                }
            )
        }

        if (showStartTimeDialog) {
            TimePickerDialogDial(
                title = "Start time",
                onConfirm = { timePickerState ->
                    startTime = timePickerState.hour to timePickerState.minute
                    timeError = ""
                },
                onDismiss = {
                    showStartTimeDialog = false
                }
            )
        }

        if (showEndTimeDialog) {
            TimePickerDialogDial(
                title = "End time",
                onConfirm = { timePickerState ->
                    endTime = timePickerState.hour to timePickerState.minute
                    timeError = ""
                },
                onDismiss = {
                    showEndTimeDialog = false
                }
            )
        }

        // Folders pop up
        AnimatedVisibility(
            visible = showListsPopUp,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = listsOffset.x.toInt() - 16.dp.toPx().toInt(),
                        y = listsOffset.y.toInt() - listsPopupHeight - 16.dp.toPx()
                            .toInt() - statusBarHeight.toPx().toInt()
                    )
                }
        ) {
            var lineWidth by remember {
                mutableStateOf(0.dp)
            }

            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorDarkContainers)
                    .onPlaced {
                        lineWidth = with(d) { it.size.width.toDp() }
                        listsPopupHeight = it.size.height
                    }
                    .heightIn(max = listsMaxHeight)
            ) {
                itemsIndexed(listsToShow) { index, folder ->
                    Row(
                        modifier = Modifier
                            .widthIn(min = lineWidth)
                            .clickable {
                                list = folder
                                focusManager.clearFocus()
                            }
                            .padding(
                                bottom = if (index == listsToShow.count() - 1) 12.dp else 6.dp,
                                top = if (index == 0) 12.dp else 6.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = folder,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    if (index != listsToShow.count() - 1) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .background(ColorCianLight)
                                .width(lineWidth)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateTaskScreenPreview() {
    GridTaskTheme {
        CreateTaskScreen(
            state = CreateTaskScreenState(),
            onAction = {},
            navigateUp = {}
        )
    }
}