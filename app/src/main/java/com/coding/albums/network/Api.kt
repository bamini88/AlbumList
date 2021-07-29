package com.coding.albums.network

import com.coding.albums.model.Album
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("{url}")
    fun getAlbums(
        @Path("url") url: String?
    ): Call<List<Album?>?>?
    }