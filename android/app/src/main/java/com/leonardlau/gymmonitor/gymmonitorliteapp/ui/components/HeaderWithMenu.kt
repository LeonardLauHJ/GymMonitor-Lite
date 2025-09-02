package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A reusable header bar that displays a title and a button for the drawer menu.
 *
 * To make the menu button work, the screen liked to the page using this composable must be
 * wrapped inside a ModalNavigationDrawer, and that drawer must be given a `drawerContent`
 * (e.g. [MemberDrawer]) which defined the UI for the drawer.
 * The [onMenuClick] callback should link to opening the drawer with scope.launch { drawerState.open() }
 *
 * @param title The text to display in the header.
 * @param textColor The text colour, defaults to white.
 * @param backgroundColor The background colour, defaults to a bluish purple.
 * @param fontSize Text font size, default 25.sp.
 * @param fontWeight Text font weight, default Bold.
 * @param onMenuClick Callback function triggered when the menu button is pressed.
 */
@Composable
fun HeaderWithMenu(
    title: String,
    textColor: Color = Color(0xFFFFFFFF),
    backgroundColor: Color = Color(0xFF495D91),
    fontSize: androidx.compose.ui.unit.TextUnit = 25.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu button (opens/closes the drawer menu)
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Default.Menu, // Burger Menu icon
                    contentDescription = "Menu",
                    tint = Color.White // Icon color
                )
            }

            // Screen/page title to display in the header
            Text(
                text = title,
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
