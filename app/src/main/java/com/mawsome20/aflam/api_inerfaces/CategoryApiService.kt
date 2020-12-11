package com.mawsome20.aflam.api_inerfaces

import com.google.gson.JsonArray
import com.mawsome20.aflam.new_conception.models.mix_movies_series
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApiService {
    @GET("/getdatabycategory")
    fun getDataByCategory(@Query("category") category: String?,@Query("child") child: String?): Call<List<mix_movies_series>?>?
}