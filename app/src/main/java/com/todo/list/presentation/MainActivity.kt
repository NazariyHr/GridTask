package com.todo.list.presentation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todo.list.presentation.common.theme.MainBgColor
import com.todo.list.presentation.common.theme.TodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.rgb(177, 234, 242),
                Color.rgb(177, 234, 242)
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
            this@MainActivity.window.navigationBarColor = Color.rgb(177, 234, 242)
        }

        setContent {
            TodoListTheme {
                Content(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MainBgColor)
                )
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Text(
        text = "Hello!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    TodoListTheme {
        Content(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MainBgColor)
        )
    }
}