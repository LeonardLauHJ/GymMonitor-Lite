package com.leonardlau.gymmonitor.gymmonitorliteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.LandingPage
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.LoginPage
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.theme.GymMonitorLiteAppTheme
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.SignUpPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    // Create a CoroutineScope tied to this Activity, so we can launch
    // coroutines/background calls safely without blocking the UI
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Create a navController to manage which screen is shown
            val navController = rememberNavController()

            GymMonitorLiteAppTheme {
                // NavHost defines all the screens in the app and the start destination
                NavHost(
                    navController = navController,
                    startDestination = "landing" // The page shown when starting the app
                ) {
                    composable("landing") { LandingPage(navController) }
                    composable("signup") { SignUpPage(mainScope, navController) }
                    composable("login") { LoginPage(mainScope, navController) }
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
