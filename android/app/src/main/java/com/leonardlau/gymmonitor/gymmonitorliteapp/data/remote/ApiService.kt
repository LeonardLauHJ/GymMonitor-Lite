package com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote

import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.BookClassResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.DashboardResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.LoginResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupRequest
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.SignupResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.CheckAuthResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.GymClassDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MemberOverview
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.MembershipDetailsResponse
import com.leonardlau.gymmonitor.gymmonitorliteapp.data.model.TimetableEntry
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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
     * Member timetable endpoint.
     * This endpoint is accessible to member users only, and is used to get a list of
     * all upcoming classes at the member's club.
     * Requires an Authorization header with a Bearer token: `Authorization: Bearer <token>`.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return [Response] with a list of [TimetableEntry] items on success; errors in status/body
     */
    @GET("api/member/timetable")
    suspend fun getFullTimetable(
        @Header("Authorization") authHeader: String
    ): Response<List<TimetableEntry>>

    /**
     * Member view membership details endpoint.
     * This endpoint is accessible to member users only, and is used to get information
     * about the member user's current membership.
     * Requires an Authorization header with a Bearer token: `Authorization: Bearer <token>`.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return [Response] with [MembershipDetailsResponse] items on success; errors in status/body
     */
    @GET("api/member/membership")
    suspend fun getMembershipDetails(
        @Header("Authorization") authHeader: String
    ): Response<MembershipDetailsResponse>

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

    /**
     * Fetches detailed information for a single gym class.
     *
     * @param id ID of the gym class to retrieve
     * @param authHeader The authorization header containing the Bearer token
     * @return [Response] with [GymClassDetailsResponse] on success; errors in status/body
     */
    @GET("api/classes/{id}")
    suspend fun getClassDetails(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String
    ): Response<GymClassDetailsResponse>

    /**
     * Books an upcoming class for the authenticated user.
     *
     * @param id ID of the gym class to book
     * @param authHeader The authorization header containing the Bearer token
     * @return [Response] with [BookClassResponse] on success; errors in status/body
     */
    @POST("api/classes/{id}/book")
    suspend fun bookClass(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String
    ): Response<BookClassResponse>
}