package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
 * @param onNavigateDashboard Called when "Dashboard" is clicked.
 * @param onNavigateTimetable Called when "Timetable" is clicked.
 * @param drawerState The current state of the drawer (open/closed).
 * @param scope Coroutine scope used to open/close the drawer asynchronously.
 */
@Composable
fun MemberDrawer(
    onNavigateDashboard: () -> Unit,
    onNavigateTimetable: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // The drawer sheet (content that slides in from the side)
    ModalDrawerSheet {

        // Title for the drawer menu
        Text(
            "Menu",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )

        // Drawer Menu items below

        // Link to Dashboard
        NavigationDrawerItem(
            label = { Text("Dashboard") },
            selected = false, // true makes it highlight when active
            onClick = {
                // Close the drawer, then navigate to the dashboard screen
                scope.launch { drawerState.close() }
                onNavigateDashboard()
            }
        )

        // Link to Timetable
        NavigationDrawerItem(
            label = { Text("Timetable") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateTimetable()
            }
        )

    }
}
