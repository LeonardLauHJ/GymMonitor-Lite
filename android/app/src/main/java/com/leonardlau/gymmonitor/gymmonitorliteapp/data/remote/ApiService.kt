package com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote

import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
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
}