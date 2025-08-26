package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.local.UserPreferences
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.EmailInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.LinkText
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PasswordInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.SubmitButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * LoginPage
 * Screen for logging in to an existing user account.
 *
 * @param mainScope CoroutineScope from the Activity, used to run network calls in the background
 * @param navController NavController used to navigate between screens.
 */
@Composable
fun LoginPage(
    mainScope: CoroutineScope,
    navController: NavController,
    userPrefs: UserPreferences
) {
    // Get the current Android context
    // this will be used for showing Toast status messages
    val context = LocalContext.current

    // State variables for each input field
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State variable to track if password is visible
    var passwordVisible by remember { mutableStateOf(false) }

    // State variable to track if a login request is currently in progress
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(2f))

        // Screen Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Log In",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Input fields
        // Typing into these fields will update the corresponding state variables

        EmailInputField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )

        PasswordInputField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button to send the Log In request
        SubmitButton(
            text = "Log In",
            isLoading = isLoading,
            onClick = {
                mainScope.launch {
                    // Ensure that all fields are filled, otherwise show an error Toast message
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    // Indicate that the login request is now loading
                    isLoading = true

                    // Make the login request to the backend
                    // If successful, save the returned JWT token
                    loginUser(email, password, context, userPrefs)

                    // Indicate that the login request has finished/is no longer loading
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Link to Log In page
        LinkText(
            text = "Don't have an account? Sign up",
            onClick = { navController.navigate("signup") }
        )

        Spacer(modifier = Modifier.weight(3f))
    }
}

/**
 * Makes a request to the backend login endpoint with the filled in form details.
 */
private suspend fun loginUser(
    email: String,
    password: String,
    context: android.content.Context,
    userPrefs: UserPreferences
) {
    try {
        // Create the request object with the filled in form details.
        val request = LoginRequest(email, password)

        // Make a POST request to the login endpoint with the given details using Retrofit
        // (it will run in a coroutine so it doesn't block the UI)
        val response = RetrofitClient.apiService.login(request)

        // If the request was successful (response has status 200 OK)
        if (response.isSuccessful) {
            // Get the response, which should contain the JWT token for authorising as the user
            val loginResponse = response.body()!! // !! asserts that it will be non-null

            // Save the JWT token locally
            userPrefs.saveToken(loginResponse.token)

            // Show a success Toast message
            Toast.makeText(context, loginResponse.token, Toast.LENGTH_LONG).show()
        } else {
            // If the API returned an error
            // get the JSON error message from the backend and then parse it
            val errorJson = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
            // Show the error message in a Toast message
            Toast.makeText(context, errorResponse.error, Toast.LENGTH_LONG).show()
        }

    } catch (e: Exception) {
        // If an error occurred, log the error to the console
        e.printStackTrace()
        // Show a generic failure message to the user
        Toast.makeText(context, "Login failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}