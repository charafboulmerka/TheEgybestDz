package com.mawsome20.aflam.api_inerfaces

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SerieApiService {
    @GET("/searchserie")
    fun getSerie(@Query("title") title: String?): Call<JsonArray?>?
}