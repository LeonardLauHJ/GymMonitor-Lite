package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import retrofit2.Response

/**
 * Repository class that handles all authentication-related requests to the backend.
 * This is responsible for making network calls and parsing responses.
 */
class AuthRepository {

    /**
     * Makes a request to the backend signup endpoint with the provided details.
     *
     * @param request The details required for a signup request (name, email, password, etc).
     * @return A [Result] object containing a success message if the request succeeded,
     *         or an exception with an error message if it failed.
     */
    suspend fun signup(
        request: SignupRequest
    ): Result<String> {
        return try {
            // Send a POST request to the signup endpoint with the provided details
            val response = RetrofitClient.apiService.signup(request)

            // If the response was successful (HTTP 200 OK)
            if (response.isSuccessful) {
                // Return the success message (fallback if backend didn't send one)
                Result.success(response.body()?.message ?: "Signup successful")
            } else {
                // Parse the error JSON from the backend
                val errorJson = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)

                // Return a failure result with the backend's error message
                Result.failure(Exception(errorResponse.error))
            }
        } catch (e: Exception) {
            // If something unexpected happened (e.g., no internet)
            Result.failure(e)
        }
    }
}
