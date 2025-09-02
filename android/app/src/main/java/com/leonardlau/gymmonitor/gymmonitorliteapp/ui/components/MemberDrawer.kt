package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.PrimaryBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The UI content of a navigation drawer, used on **member-only pages**.
 * Provides navigation options (e.g. Dashboard, Timetable) for members to move
 * between different app sections they are allowed to access.
 *
 * This composable only defines the **drawer’s look and items** — it does not handle
 * the sliding mechanics or gestures. For it to work properly, the screen must be
 * wrapped in a ModalNavigationDrawer, and this `MemberDrawer` must be supplied
 * as the `drawerContent`.
 *
 * @param onNavigateDashboard Function to navigate to the Dashboard screen.
 * @param onNavigateTimetable Function to navigate to the Timetable screen.
 * @param onLogout Function to log the user out.
 * @param drawerState The current state of the drawer (open/closed).
 * @param scope Coroutine scope used to open/close the drawer asynchronously.
 */
@Composable
fun MemberDrawer(
    onNavigateDashboard: () -> Unit,
    onNavigateTimetable: () -> Unit,
    onLogout: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // The drawer sheet (content that slides in from the side)
    ModalDrawerSheet(
        drawerContainerColor = PrimaryBlue,
        drawerShape = RectangleShape
    ) {
        // Drawer header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 30.dp, bottom = 24.dp)
        ) {
            Text(
                "GymMonitor Lite",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Drawer Menu Items, with names and a corresponding icon to show beside them
        val menuItems = listOf(
            "Dashboard" to Icons.Default.Home,
            "Timetable" to Icons.Default.CalendarToday
        )

        // Create a NavigationDrawerItem for each of the menuItems
        menuItems.forEach { (label, icon) ->
            NavigationDrawerItem(
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(label, fontSize = 20.sp, color = Color.White)
                    }
                },
                selected = false,
                onClick = {
                    // Close the drawer
                    scope.launch { drawerState.close() }
                    // What to do after clicking, based on which item was clicked
                    when (label) {
                        "Dashboard" -> onNavigateDashboard()
                        "Timetable" -> onNavigateTimetable()
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Push logout to bottom
        Spacer(modifier = Modifier.weight(1f))

        // Logout button
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Logout", fontSize = 20.sp, color = Color.White)
                }
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    onLogout()
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
