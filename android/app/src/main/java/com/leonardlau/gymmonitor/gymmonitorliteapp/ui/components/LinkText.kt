package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.LinkBlue

/**
 * A reusable clickable text composable, styled as a hyperlink.
 * To be used for navigation or other actions that resemble web links.
 *
 * @param text The text to display.
 * @param onClick Callback function to run when the text is clicked.
 *                e.g. onClick = { navController.navigate("login") }
 * @param color The text colour, defaults to a standard blue colour for links.
 * @param fontSize Text font size, default 16.sp.
 * @param fontWeight Text font weight, default Normal.
 * @param modifier Optional Modifier for layout/styling.
 */
@Composable
fun LinkText(
    text: String,
    onClick: () -> Unit,
    color: Color = LinkBlue,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onClick() }
        )
    }
}
