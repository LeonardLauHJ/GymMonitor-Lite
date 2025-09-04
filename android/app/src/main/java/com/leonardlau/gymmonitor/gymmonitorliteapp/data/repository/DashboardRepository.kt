package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class that handles all dashboard-related requests to the backend.
 */
class DashboardRepository {

    /**
     * Fetches the member dashboard from the backend.
     *
     * @param token JWT token for authentication
     * @return Result containing dashboard data or exception with error message
     */
    suspend fun getDashboard(token: String): Result<DashboardResponse> {
        return try {
            // Send a GET request to the dashboard endpoint with "Bearer <token>"
            // in the Authorization header
            val response = RetrofitClient.apiService.getDashboard("Bearer $token")

            if (response.isSuccessful) {
                // Set the Result as a success with the response body containing the dashboard data
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
