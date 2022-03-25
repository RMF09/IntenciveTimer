package com.example.intencivetimer.core.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.intencivetimer.R
import com.example.intencivetimer.core.ui.theme.IntenciveTimerTheme
import com.example.intencivetimer.core.ui.theme.buttonRoundedCornerSize

@Composable
fun ITIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val iconButtonBackground = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(buttonRoundedCornerSize))
            .background(iconButtonBackground)

    ) {
        content()
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Preview() {
    IntenciveTimerTheme {
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.padding(8.dp)) {
                ITIconButton(
                    onClick = {},
                    content = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = stringResource(id = R.string.select_icon),
                            tint = if (isSystemInDarkTheme()) Color.LightGray else Color.Black
                        )
                    })
            }
        }
    }
}