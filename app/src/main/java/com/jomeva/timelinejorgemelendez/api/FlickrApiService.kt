package com.jomeva.timelinejorgemelendez.api


import com.jomeva.timelinejorgemelendez.response.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {
    @GET("/services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("tags") tags: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int
    ): FlickrResponse
}
