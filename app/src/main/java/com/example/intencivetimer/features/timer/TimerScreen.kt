package com.example.intencivetimer.features.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.intencivetimer.R
import com.example.intencivetimer.core.ui.theme.IntenciveTimerTheme

@Composable
fun TimerScreen(navController: NavController) {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.timer))
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(id = R.string.timer), color = Color.Gray)
        }
    }
}

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun Preview() {
    IntenciveTimerTheme {
        Surface(color = MaterialTheme.colors.background) {
            ScreenContent()
        }
    }
}