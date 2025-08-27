package com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote

import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.CheckAuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Defines all API endpoints that the app can call.
 *
 * Each function here corresponds to one HTTP request,
 * and Retrofit will automatically generate the implementation.
 */
interface ApiService {
    /**
     * Signup endpoint.
     * @return Response<SignupResponse> to handle success and error separately
     */
    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    /**
     * Login endpoint.
     * @return Response<LoginResponse> to handle success and error separately
     */
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Check authorisation endpoint.
     * This endpoint is used to check the authorisation of the current user.
     * If logged-in, it will give a 200 OK with the user's id, name and role,
     * otherwise it responds with a 401 Unauthorized with an error message.
     *
     * @return Response<CheckAuthResponse> to handle success and error separately
     */
    @GET("api/auth/check")
    suspend fun checkAuth(
        @Header("Authorization") authHeader: String
    ): Response<CheckAuthResponse>
}