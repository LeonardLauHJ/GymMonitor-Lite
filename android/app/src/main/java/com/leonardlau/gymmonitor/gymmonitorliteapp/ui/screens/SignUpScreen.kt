package com.leonardlau.gymmonitor.gymmonitorliteapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository.AuthRepository
import com.leonardlau.gymmonitor.gymmonitorliteapp.ui.viewmodel.SignUpViewModel

/**
 * Connects the [SignUpPage] UI to the [SignUpViewModel], which provides state and logic.
 */
@Composable
fun SignUpScreen(
    navController: NavController
) {
    // Repository which handles the backend API calls
    val repository = AuthRepository()

    // Get the current Android context, used for showing Toast status messages
    val context = LocalContext.current

    // Create the ViewModel to hold the state and logic for the signup form
    val viewModel: SignUpViewModel = viewModel(
        // Make a custom ViewModel factory so we can pass in our own repository
        factory = object : ViewModelProvider.Factory {
            // Create and return a SignUpViewModel instance
            // Ignore the cast warning, we know the result will be a SignUpViewModel
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignUpViewModel(repository) as T
            }
        }
    )

    // Call the UI and pass in everything it needs
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
            // Use the viewmodel's signup function when the submit button is pressed,
            // and what to do after its successful (error cases are handled by signup function)
            viewModel.signUp { success, message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                if (success) {
                    navController.navigate("login")
                }
            }
        },
        onNavigateToLogin = { navController.navigate("login") }
    )
}
