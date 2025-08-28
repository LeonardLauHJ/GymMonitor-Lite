package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel

/**
 * A composable screen that connects the [SignUpPage] UI with the [SignUpViewModel].
 * It exposes state from the ViewModel to the UI and passes user interactions
 * back to the ViewModel for handling.
 *
 * @param navController Used for navigating between screens.
 */
@Composable
fun SignUpScreen(
    navController: NavController
) {
    // Get the current Android context, used for showing Toast status messages
    val context = LocalContext.current

    // Get the ViewModel to hold the state and logic for the signup form
    val viewModel: SignUpViewModel = viewModel()

    // Render the signup screen UI with state from the ViewModel
    SignUpPage(
        name = viewModel.name,
        email = viewModel.email,
        password = viewModel.password,
        clubCode = viewModel.clubCode,
        membershipPlanId = viewModel.membershipPlanId,
        isLoading = viewModel.isLoading,
        onNameChange = { viewModel.name = it },
        onEmailChange = { viewModel.email = it },
        onPasswordChange = { viewModel.password = it },
        onClubCodeChange = { viewModel.clubCode = it },
        onMembershipPlanChange = { viewModel.membershipPlanId = it },
        onSignUpClick = {
            // Call the viewmodel's signup function when the submit button is pressed,
            // and handle what to do if its successful (error cases are handled by signup function)
            viewModel.signUp { success, message ->
                // Show a success Toast message and navigate to the login page
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                if (success) {
                    navController.navigate("login")
                }
            }
        },
        onNavigateToLogin = { navController.navigate("login") }
    )
}
