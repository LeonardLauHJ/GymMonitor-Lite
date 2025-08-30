package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class that handles all requests to the backend for the club members overview.
 */
class ClubMembersOverviewRepository {

    /**
     * Fetches the club members overview from the backend.
     *
     * @param token JWT token for authentication
     * @return Result containing a list of member overview data, or exception with error message
     */
    suspend fun getClubMembersOverview(token: String): Result<List<MemberOverview>> {
        return try {
            // Send a POST request to the club members overview (Staff view club members)
            // endpoint with "Bearer <token>" in the Authorization header
            val response = RetrofitClient.apiService.getClubMembersOverview("Bearer $token")

            if (response.isSuccessful) {
                // Set the Result as a success with the response body containing the data
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
