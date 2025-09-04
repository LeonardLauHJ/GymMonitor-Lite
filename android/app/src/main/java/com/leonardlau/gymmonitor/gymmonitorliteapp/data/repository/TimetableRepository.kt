package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class that handles all requests to the backend for the member timetables.
 */
class TimetableRepository {

    /**
     * Fetches the full list of upcoming classes at the member's club from the backend.
     *
     * @param token JWT token for authentication
     * @return Result containing a list of timetable entries, or exception with error message
     */
    suspend fun getFullTimetable(token: String): Result<List<TimetableEntry>> {
        return try {
            // Send a GET request to the timetable endpoint (no date) with
            // "Bearer <token>" in the Authorization header
            val response = RetrofitClient.apiService.getFullTimetable("Bearer $token")

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
