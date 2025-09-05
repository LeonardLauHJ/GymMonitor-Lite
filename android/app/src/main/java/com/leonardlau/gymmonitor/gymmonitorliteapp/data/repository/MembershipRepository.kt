package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MembershipDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class handling requests for membership-related data from the backend.
 */
class MembershipRepository {

    /**
     * Fetches the details of the currently authenticated member's membership.
     *
     * @param token JWT token for authentication
     * @return Result containing membership details or exception with error message
     */
    suspend fun getMembershipDetails(token: String): Result<MembershipDetailsResponse> {
        return try {
            // Send a GET request to the membership details endpoint with "Bearer <token>"
            // in the Authorization header
            val response = RetrofitClient.apiService.getMembershipDetails("Bearer $token")

            if (response.isSuccessful) {
                // Set the Result as a success with the response body containing the membership data
                Result.success(response.body()!!)
            } else {
                // Parse the error JSON from the backend
                val errorJson = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)

                // Return a failure result with the backend's error message
                Result.failure(Exception(errorResponse.error))
            }
        } catch (e: Exception) {
            // If something unexpected happened
            Result.failure(e)
        }
    }
}