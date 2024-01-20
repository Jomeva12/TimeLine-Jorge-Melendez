package com.jomeva.timelinejorgemelendez.api

import com.jomeva.timelinejorgemelendez.model.Photos
import com.jomeva.timelinejorgemelendez.response.FlickrResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConstants {

    private val FLICKR_API = "40bd373bb6a19a078023b06af055d03c"
    private val FLICKR_FORMAT = "json"
    private val FLICKR_JSON = 1
    private val FLICKR_GET_METHOD = "flickr.photos.search"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.flickr.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val flickrApiService: FlickrApiService = retrofit.create(FlickrApiService::class.java)

    suspend fun searchPhotos(tags: String): FlickrResponse {
        return try {
            flickrApiService.searchPhotos(
                FLICKR_GET_METHOD,
                FLICKR_API,
                tags,
                FLICKR_FORMAT,
                FLICKR_JSON
            )
        } catch (e: Exception) {
            FlickrResponse(Photos(emptyList()))
        }
    }
}
