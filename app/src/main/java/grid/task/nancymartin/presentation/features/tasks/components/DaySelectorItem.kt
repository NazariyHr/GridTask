package grid.task.nancymartin.presentation.features.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grid.task.nancymartin.presentation.common.theme.ColorDarkContainers
import grid.task.nancymartin.presentation.common.theme.ColorLightContainers
import grid.task.nancymartin.presentation.common.theme.GridTaskTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by nazar at 15.08.2024
 */
@Composable
fun DaySelectorItem(
    dateTime: Long,
    isSelected: Boolean,
    onDaySelected: (dateTime: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val yearAndMonthStr by remember(dateTime) {
        derivedStateOf {
            val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            formatter.format(Date(dateTime))
        }
    }
    val dayStr by remember(dateTime) {
        derivedStateOf {
            val formatter = SimpleDateFormat("EEE, dd", Locale.getDefault())
            formatter.format(Date(dateTime))
        }
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onDaySelected(dateTime)
            }
            .background(
                color = if (isSelected) ColorDarkContainers else ColorLightContainers,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 6.dp, horizontal = 16.dp)
    ) {
        Text(
            text = yearAndMonthStr,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = dayStr,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun DaySelectorItemSelectedPreview() {
    GridTaskTheme {
        DaySelectorItem(
            dateTime = Calendar.getInstance().timeInMillis,
            isSelected = true,
            onDaySelected = {}
        )
    }
}

@Preview
@Composable
private fun DaySelectorItemUnselectedPreview() {
    GridTaskTheme {
        DaySelectorItem(
            dateTime = Calendar.getInstance().timeInMillis,
            isSelected = false,
            onDaySelected = {}
        )
    }
}