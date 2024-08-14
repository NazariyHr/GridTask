package grid.task.nancymartin.presentation.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


val TextColorDefault = Color(0, 61, 70, 255)
val TextColorForHeadLinesAndDisplay = Color(1, 25, 29, 255)

val Typography = run {
    val t = Typography()
    Typography(
        displayLarge = t.displayLarge.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        displayMedium = t.displayMedium.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        displaySmall = t.displaySmall.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        headlineLarge = t.headlineLarge.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        headlineMedium = t.headlineMedium.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        headlineSmall = t.headlineSmall.copy(
            color = TextColorForHeadLinesAndDisplay
        ),
        titleLarge = t.titleLarge.copy(
            color = TextColorDefault
        ),
        titleMedium = t.titleMedium.copy(
            color = TextColorDefault
        ),
        titleSmall = t.titleSmall.copy(
            color = TextColorDefault
        ),
        bodyLarge = t.bodyLarge.copy(
            color = TextColorDefault
        ),
        bodyMedium = t.bodyMedium.copy(
            color = TextColorDefault
        ),
        bodySmall = t.bodySmall.copy(
            color = TextColorDefault
        ),
        labelLarge = t.labelLarge.copy(
            color = TextColorDefault
        ),
        labelMedium = t.labelMedium.copy(
            color = TextColorDefault
        ),
        labelSmall = t.labelSmall.copy(
            color = TextColorDefault
        )
    )
}

@Preview
@Composable
private fun FontsPreviewOnMainBackgroundColor() {
    GridTaskTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorMainBg)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Display Large",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Display Medium",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "Display Small",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Headline Small",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Title Medium",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Title Small",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Body Medium",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Body Small",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Label Large",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Label Medium",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Label Small",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun FontsPreviewInLightContainers() {
    GridTaskTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorMainBg)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorLightContainers)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Display Large",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Display Medium",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "Display Small",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Headline Small",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Title Medium",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Title Small",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Body Medium",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Body Small",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Label Large",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Label Medium",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Label Small",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun FontsPreviewInDarkContainers() {
    GridTaskTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorMainBg)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorDarkContainers)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Display Large",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Display Medium",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "Display Small",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Headline Small",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Title Medium",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Title Small",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Body Medium",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Body Small",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Label Large",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Label Medium",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Label Small",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}