package com.leonardlau.gymmonitor.gymmonitorliteapp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * SignUpPage
 * Screen for signing-up/registering new Member user accounts.
 * Uses a coroutine (passed from the Activity) to call the backend in the background
 * so the UI can stay responsive while waiting for network responses.
 */
@Composable
fun SignUpPage(mainScope: CoroutineScope) {
    // Get the current Android context
    // this will be used for showing Toast status messages
    val context = LocalContext.current

    // State variables for each input field
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var clubCode by remember { mutableStateOf("") }
    var membershipPlanId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Signup",
                fontSize = 25.sp
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
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )

        // Signup button
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

                    // If the previous checks passed, make the signup request to the backend
                    signupUser(name, email, password, clubCode, planId, context)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Signup")
        }
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

        if (response.isSuccessful) {
            // If the signup request was successful (response has status 200 OK)
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