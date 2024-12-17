package grid.task.nancymartin.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.R
import grid.task.nancymartin.domain.utils.toFormattedDate
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    title: String,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val headline by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis?.toFormattedDate() ?: title
        }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.OK))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 12.dp,
                        top = 16.dp
                    )
                )
            },
            headline = {
                Text(
                    text = headline,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 12.dp,
                        top = 16.dp
                    )
                )
            }
        )
    }
}

@Preview
@Composable
fun DatePickerModalPreview() {
    GridTaskTheme {
        DatePickerModal(
            title = "Start date",
            onDateSelected = {},
            onDismiss = {}
        )
    }
}