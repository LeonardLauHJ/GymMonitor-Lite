package com.leonardlau.gymmonitor.gymmonitorliteapp.data.repository

import com.google.gson.Gson
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.ErrorResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote.RetrofitClient
import okio.EOFException

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
            // Send a GET request to the class details endpoint with "Bearer <token>"
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

    /**
     * Attempts to book a single gym class for the authenticated user.
     *
     * @param id ID of the class to book
     * @param token JWT token for authentication
     * @return Result containing class details or exception with error message
     */
    suspend fun bookClass(id: Int, token: String): Result<String> {
        return try {
            // Send a POST request to the book class endpoint with "Bearer <token>"
            // in the Authorization header
            val response = RetrofitClient.apiService.bookClass(id, "Bearer $token")

            /*
            NOTE: I initially used the same code as in my other repositories, but for this endpoint
            the error cases sometimes threw a strange error (\n not found limit=1 content=0d…)
            instead of the expected backend message. This likely happened because OkHttp
            (the HTTP client Retrofit uses) occasionally got the server's response cut off too
            early, causing an EOFException when reading it (i.e. the HTTP response ended before
            OkHttp could fully read it). From what I've found, this seems to randomly happen with
            OkHttp when a POST request returns a small response (e.g. a short JSON message like here)
            To fix this, I made it so that if I got an EOFException, I retry the request until it
            completes without an EOFException, and this seems to fix the issue consistently.
            I currently don't have a limit on how many times to retry the request, which would be good,
            however it is extremely unlikely that it will keep recursing for a long period of time.
            In practice, it usually succeeds on the first retry, sometimes the second.
            */

            // If the response was successful
            if (response.isSuccessful) {
                // Return the success message (fallback if backend didn't send one)
                Result.success(response.body()?.message ?: "Successfully booked class")
            } else {
                // Parse the error JSON from the backend
                val errorJson = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)

                // Return a failure result with the backend's error message
                Result.failure(Exception(errorResponse.error))
            }
        } catch (e: EOFException) {
            // Special case: OkHttp throws EOFException if the HTTP stream is cut off mid-read.
            // This appears to be a somewhat randomly occurring issue
            // My solution is to retry the request until an EOFException does not occur
            return bookClass(id, token)
        } catch (e: Exception) {
            // Catch any other exception with a generic fallback message
            Result.failure(Exception("Failed to book class"))
        }
    }

}