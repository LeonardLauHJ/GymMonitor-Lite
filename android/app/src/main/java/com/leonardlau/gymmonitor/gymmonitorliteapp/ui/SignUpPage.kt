package com.leonardlau.gymmonitor.gymmonitorliteapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * SignUpPage
 * Screen for signing-up/registering new Member user accounts.
 */
@Composable
fun SignUpPage() {
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
                // Send a request to the signup endpoint with the filled details
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Signup")
        }
    }
}
