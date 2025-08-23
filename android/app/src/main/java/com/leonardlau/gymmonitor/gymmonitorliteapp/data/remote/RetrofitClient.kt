package com.leonardlau.gymmonitor.gymmonitorliteapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient - singleton object that sets up Retrofit for the app.
 *
 * Retrofit is the library for talking to the backend API.
 * It handles sending HTTP requests (GET, POST, etc.) and converting JSON responses into
 * Kotlin objects automatically. This is all done asynchronously.
 *
 * This object creates one instance of Retrofit and provides ApiService, which lets us
 * call the API from anywhere in the app without creating multiple instances.
 */

object RetrofitClient {

    // Base URL of the backend API
    // NOTE: the backend uses the URL http://localhost:8080/ on the host machine (the PC)
    // However, if we use localhost here inside the emulator, it would point to
    // the emulator itself rather than the PC. Instead, we can use 10.0.2.2,
    // a magic IP which lets us reach the host machine.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Single instance used to make API calls throughout the app
    // The endpoints which can be called are defined in the ApiService Interface
    val apiService: ApiService by lazy { // lazy initialisation so it's only created once and reused
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Tell Retrofit where our API is hosted
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson to handle JSON
            .build() // Build the Retrofit instance
            .create(ApiService::class.java) // Generate an implementation of ApiService
    }
}