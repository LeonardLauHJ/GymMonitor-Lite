package com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote

import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.CheckAuthResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview
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
     * Registers a new user with the provided information.
     *
     * @param request The signup request body containing user details.
     * @return [Response] with [SignupResponse] on success; errors in status/body
     */
    @POST("api/auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<SignupResponse>

    /**
     * Login endpoint.
     * Authenticates a user with email and password.
     *
     * @param request The login request body containing credentials.
     * @return [Response] with [LoginResponse] on success; errors in status/body
     */
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * Check authorisation endpoint.
     * Checks the authorisation of the current user.
     * If logged-in, returns 200 OK with the user's id, name and role,
     * otherwise 401 Unauthorized with an error message.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return [Response] with [CheckAuthResponse] on success; errors in status/body
     */
    @GET("api/auth/check")
    suspend fun checkAuth(
        @Header("Authorization") authHeader: String
    ): Response<CheckAuthResponse>

    /**
     * Member dashboard endpoint.
     * This endpoint is accessible to member users only, and is used to get the data
     * to display on the user's dashboard screen.
     * Requires an Authorization header with a Bearer token: `Authorization: Bearer <token>`.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return [Response] with [DashboardResponse] on success; errors in status/body
     */
    @GET("api/member/dashboard")
    suspend fun getDashboard(
        @Header("Authorization") authHeader: String
    ): Response<DashboardResponse>

    /**
     * Club Members Overview endpoint.
     * This endpoint is accessible to staff users only, and is used to get the data
     * showing an overview of all members at club which the staff user belongs to.
     * Requires an Authorization header with a Bearer token: `Authorization: Bearer <token>`.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return [Response] with a list of [MemberOverview] items on success; errors in status/body
     */
    @GET("api/staff/members")
    suspend fun getClubMembersOverview(
        @Header("Authorization") authHeader: String
    ): Response<List<MemberOverview>>
}