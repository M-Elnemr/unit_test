package com.elnemr.unittest.data.remote

import com.elnemr.unittest.BuildConfig
import com.elnemr.unittest.data.remote.response.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}