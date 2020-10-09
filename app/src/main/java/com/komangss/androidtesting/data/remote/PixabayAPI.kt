package com.komangss.androidtesting.data.remote

import com.komangss.androidtesting.BuildConfig
import com.komangss.androidtesting.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchImage : String,
        @Query("key") apiKey : String = BuildConfig.API_KEY
    ) : Response<ImageResponse>
}