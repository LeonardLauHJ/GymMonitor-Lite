package com.leonardlau.gymmonitor.gymmonitorliteapp.ui

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
                    // Convert the entered membership plan ID into an int
                    val planId = membershipPlanId.toIntOrNull()
                    if (planId != null) {
                        signupUser(name, email, password, clubCode, planId)
                    } else {
                        // Error
                    }

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
    membershipPlanId: Int
) {
    try {
        // Create the request object with the filled in form details.
        val request = SignupRequest(name, email, password, clubCode, membershipPlanId)

        // Make a POST request to the signup endpoint with the given details using Retrofit
        // (it will run in a coroutine so it doesn't block the UI)
        val response = RetrofitClient.apiService.signup(request)

    } catch (e: Exception) {
        // If an error occurred, log the error to the console
        e.printStackTrace()
    }
}