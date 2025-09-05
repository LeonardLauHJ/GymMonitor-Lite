package com.leonardlau.gymmonitor.gymmonitorliteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.ClassDetailsScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.ClubMembersOverviewScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.DashboardScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.LandingPage
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.LoginScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.MembershipDetailsScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.ProtectedScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.SignUpScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens.TimetableScreen
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.GymMonitorLiteAppTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    // Create a CoroutineScope tied to this Activity, so we can launch
    // coroutines/background calls safely without blocking the UI
    private val mainScope = MainScope()

    // UserPreferences instance for handling locally stored data such as JWT tokens
    // Initialized later in onCreate() because it needs the Activity context
    private lateinit var userPrefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authRepository = AuthRepository()

        setContent {
            // Create a navController to manage which screen is shown
            val navController = rememberNavController()

            // Create the UserPreferences instance tied to this Activity's Context
            // This will manage storing and retrieving data locally such as JWT tokens
            userPrefs = UserPreferences(this)

            GymMonitorLiteAppTheme {
                // NavHost defines all the screens in the app and the start destination
                NavHost(
                    navController = navController,
                    startDestination = "landing" // The page shown when starting the app
                ) {
                    composable("landing") { LandingPage(navController) }
                    composable("signup") { SignUpScreen(navController) }
                    composable("login") { LoginScreen(navController, userPrefs) }

                    composable("dashboard") {
                        // Wrap in a ProtectedScreen so only authenticated users with
                        // the required role can access it
                        ProtectedScreen(
                            requiredRole = "MEMBER",
                            navController = navController,
                            userPrefs = userPrefs,
                            authRepository = authRepository
                        ) {
                            DashboardScreen(navController, userPrefs)
                        }
                    }

                    composable("timetable") {
                        ProtectedScreen(
                            requiredRole = "MEMBER",
                            navController = navController,
                            userPrefs = userPrefs,
                            authRepository = authRepository
                        ) {
                            TimetableScreen(navController, userPrefs)
                        }
                    }

                    composable("membership") {
                        ProtectedScreen(
                            requiredRole = "MEMBER",
                            navController = navController,
                            userPrefs = userPrefs,
                            authRepository = authRepository
                        ) {
                            MembershipDetailsScreen(navController, userPrefs)
                        }
                    }

                    composable("clubMembersOverview") {
                        ProtectedScreen(
                            requiredRole = "STAFF",
                            navController = navController,
                            userPrefs = userPrefs,
                            authRepository = authRepository
                        ) {
                            ClubMembersOverviewScreen(navController, userPrefs)
                        }
                    }

                    composable(
                        route = "classDetails/{classId}", // dynamic argument, classId can be any int
                        // Declares classId as a required argument that must be an Int
                        arguments = listOf(navArgument("classId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        // To get classId, we need to retrieve it from backStackEntry,
                        // which represents this screen in the navigation history (back stack)
                        val classId = backStackEntry.arguments?.getInt("classId") ?: 0

                        ProtectedScreen(
                            requiredRole = null, // no specific role required, any logged-in user can access
                            navController = navController,
                            userPrefs = userPrefs,
                            authRepository = authRepository
                        ) {
                            ClassDetailsScreen(classId, navController, userPrefs)
                        }
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all coroutines to prevent memory leaks and further API calls
        mainScope.cancel()
    }
}
