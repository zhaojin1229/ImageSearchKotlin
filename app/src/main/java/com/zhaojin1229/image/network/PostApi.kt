package com.zhaojin1229.image.network

import com.zhaojin1229.image.model.response.ImageData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PostApi {
    @GET("Search/ImageSearchAPI")
    fun getPosts(@Header("x-rapidapi-host") x: String, @Header("x-rapidapi-key") y:String, @Query("pageNumber")pageNumber: Int , @Query("pageSize")pageSize: Int, @Query("q")q: String, @Query("safeSearch")safeSearch: Boolean): Observable<ImageData>
}