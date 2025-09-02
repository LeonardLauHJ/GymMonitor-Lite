package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class handling requests for class-related data from the backend.
 */
class ClassRepository {

    /**
     * Fetches the details for a single gym class.
     *
     * @param id ID of the class to fetch
     * @param token JWT token for authentication
     * @return Result containing class details or exception with error message
     */
    suspend fun getClassDetails(id: Int, token: String): Result<GymClassDetailsResponse> {
        return try {
            // Send a POST request to the dashboard endpoint with "Bearer <token>"
            // in the Authorization header
            val response = RetrofitClient.apiService.getClassDetails(id, "Bearer $token")

            if (response.isSuccessful) {
                // Set the Result as a success with the response body containing the class data
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