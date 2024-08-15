package grid.task.nancymartin.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme

/**
 * Created by nazar at 15.08.2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectableTextField(
    modifier: Modifier = Modifier,
    selectedValueStr: String?,
    label: String,
    unselectOptionStr: String,
    unselectOption: T,
    options: List<T>,
    onValueChange: (T?) -> Unit,
    optionsBackgroundColor: Color = Color.Transparent,
    optionContent: @Composable BoxScope.(T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val value by remember(selectedValueStr) {
        derivedStateOf {
            selectedValueStr ?: unselectOptionStr
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = value,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .background(optionsBackgroundColor)
                .exposedDropdownSize()
        ) {
            DropdownMenuItem(
                text = {
                    Box {
                        optionContent(unselectOption)
                    }
                },
                onClick = {
                    expanded = false
                    onValueChange(null)
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Box {
                            optionContent(option)
                        }
                    },
                    onClick = {
                        expanded = false
                        onValueChange(option)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectableTextFieldPreview() {
    GridTaskTheme {
        MainScreensLayout {
            SelectableTextField(
                selectedValueStr = "Option 1",
                label = "Label text",
                options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
                unselectOption = null,
                unselectOptionStr = "All lists",
                onValueChange = {},
                optionContent = { option ->
                    Text(text = option.orEmpty())
                }
            )
        }
    }
}