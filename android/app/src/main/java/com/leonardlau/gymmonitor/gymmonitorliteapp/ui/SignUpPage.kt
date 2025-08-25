package com.leonardlau.gymmonitor.gymmonitorliteapp.ui

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
fun SignUpPage(mainScope: CoroutineScope, navController: NavController) {
    // Get the current Android context
    // this will be used for showing Toast status messages
    val context = LocalContext.current

    // State variables for each input field
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var clubCode by remember { mutableStateOf("") }
    var membershipPlanId by remember { mutableStateOf("") }

    // State variable to track if password is visible
    var passwordVisible by remember { mutableStateOf(false) }

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

        OutlinedTextField(
            // text field value is tied to the 'name' state variable
            value = name,
            // whenever the text value changes, update the state variable
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            // For on-screen keyboard, use the email keyboard type (has easy access to @ symbol)
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            // Show password text if passwordVisible is true, otherwise mask it
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            // Eye icon button at the end for toggling visibility
            trailingIcon = {
                // The image icon to use will depend on the value of passwordVisible
                val image = if (passwordVisible)
                    Icons.Default.Visibility
                else Icons.Default.VisibilityOff

                // When the icon is clicked, flip the value of the passwordVisible state variable
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    // Update the icon's image and description accordingly
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        OutlinedTextField(
            value = clubCode,
            onValueChange = { clubCode = it },
            label = { Text("Club Code") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = membershipPlanId,
            onValueChange = { membershipPlanId = it },
            label = { Text("Membership Plan ID") },
            modifier = Modifier.fillMaxWidth(),
            // For on-screen keyboard, use the numeric keypad
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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