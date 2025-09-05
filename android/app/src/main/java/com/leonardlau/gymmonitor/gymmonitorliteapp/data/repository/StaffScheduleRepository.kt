package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.StaffScheduleEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient

/**
 * Repository class that handles all requests to the backend for the staff schedule.
 */
class StaffScheduleRepository {

    /**
     * Fetches the full list of upcoming classes being instructed by the currently authenticated
     * staff user from the backend.
     *
     * @param token JWT token for authentication
     * @return Result containing a list of schedule entries, or exception with error message
     */
    suspend fun getFullSchedule(token: String): Result<List<StaffScheduleEntry>> {
        return try {
            // Send a GET request to the timetable endpoint (no date) with
            // "Bearer <token>" in the Authorization header
            val response = RetrofitClient.apiService.getFullStaffSchedule("Bearer $token")

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
