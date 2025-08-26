package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.EmailInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.PasswordInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.NumberInputField
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.components.TextInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * SignUpPage
 * Screen for signing-up/registering new Member user accounts.
 *
 * @param mainScope CoroutineScope from the Activity, used to run network calls in the background
 * @param navController NavController used to navigate between screens.
 */
@Composable
fun SignUpPage(
    mainScope: CoroutineScope,
    navController: NavController
) {
    // Get the current Android context
    // this will be used for showing Toast status messages
    val context = LocalContext.current

    // State variables for each input field
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var clubCode by remember { mutableStateOf("") }
    var membershipPlanId by remember { mutableStateOf("") }

    // State variable to track if a signup request is currently in progress
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Screen Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Input fields
        // Typing into these fields will update the corresponding state variables

        TextInputField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            modifier = Modifier.fillMaxWidth()
        )

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

        TextInputField(
            value = clubCode,
            onValueChange = { clubCode = it },
            label = "Club Code",
            modifier = Modifier.fillMaxWidth()
        )

        NumberInputField(
            value = membershipPlanId,
            onValueChange = { membershipPlanId = it },
            label = "Membership Plan ID",
            modifier = Modifier.fillMaxWidth()
        )

        // Submit Sign Up button
        Button(
            onClick = {
                mainScope.launch {
                    // Ensure that all fields are filled, otherwise show an error Toast message
                    if (name.isBlank() || email.isBlank() || password.isBlank() || clubCode.isBlank()
                        || membershipPlanId.isBlank()) {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    // Try converting membershipPlanId into an Int
                    val planId = membershipPlanId.toIntOrNull()
                    // If the value could not be converted to int, show an error Toast message
                    if (planId == null) {
                        Toast.makeText(context, "Membership Plan ID must be a number", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    // Indicate that the signup request is now loading
                    isLoading = true

                    // Make the signup request to the backend
                    signupUser(name, email, password, clubCode, planId, context)

                    // Indicate that the signup request has finished/is no longer loading
                    isLoading = false
                }
            },
            // Disable the button if a signup request is currently in progress
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            // If a sign up request is currently in progress, display a loading spinner
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                // Otherwise display the Sign Up text
                Text(
                    text = "Sign Up",
                    fontSize = 17.sp
                )
            }
        }

        // Link to Log In page
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Already have an account? Log in",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1E88E5),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                },
            )
        }

        Spacer(modifier = Modifier.weight(3f))
    }
}

/**
 * Makes a request to the backend signup endpoint with the filled in form details.
 */
private suspend fun signupUser(
    name: String,
    email: String,
    password: String,
    clubCode: String,
    membershipPlanId: Int,
    context: android.content.Context
) {
    try {
        // Create the request object with the filled in form details.
        val request = SignupRequest(name, email, password, clubCode, membershipPlanId)

        // Make a POST request to the signup endpoint with the given details using Retrofit
        // (it will run in a coroutine so it doesn't block the UI)
        val response = RetrofitClient.apiService.signup(request)

        // If the request was successful (response has status 200 OK)
        if (response.isSuccessful) {
            val success = response.body()
            // Show a Toast message with the success message from the backend
            Toast.makeText(context, success?.message ?: "Signup successful", Toast.LENGTH_LONG).show()
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
        Toast.makeText(context, "Signup failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}