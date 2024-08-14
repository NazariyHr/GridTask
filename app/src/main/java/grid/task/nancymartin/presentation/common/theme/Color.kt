package grid.task.nancymartin.presentation.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val ColorWhite = Color(177, 234, 242)
val ColorCianLight = Color(123, 212, 225)
val ColorCian = Color(82, 191, 206)
val ColorCianDarker = Color(49, 167, 184)
val ColorCianDark = Color(19, 143, 160)

val MainBgColor = ColorWhite

/**
 * Created by nazar at 14.08.2024
 */
@Composable
fun ColorsList(
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        "ColorWhite" to ColorWhite,
        "ColorCianLight" to ColorCianLight,
        "ColorCian" to ColorCian,
        "ColorCianDarker" to ColorCianDarker,
        "ColorCianDark" to ColorCianDark,
        "ColorCianDark (ColorWhite)" to MainBgColor
    )
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = colors
        ) { color ->
            Box(
                modifier = Modifier
                    .background(
                        color = color.second,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = color.first
                )
            }
        }
    }
}

@Preview
@Composable
private fun ColorsListPreview() {
    GridTaskTheme {
        ColorsList()
    }
}