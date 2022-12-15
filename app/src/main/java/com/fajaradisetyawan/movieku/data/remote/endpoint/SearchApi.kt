/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
    ): MovieResponse

    @GET("search/tv")
    fun searchTv(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<TvResponse>

//    @GET("search/person")
//    fun searchPerson(
//        @Query("api_key") apiKey: String,
//        @Query("query") query: String
//    ): Call<SearchPersonResponse>
//
//    @GET("search/keyword")
//    fun searchKeyword(
//        @Query("api_key") apiKey: String,
//        @Query("query") query: String
//    ): Call<KeywordSearchResponse>
//
//    @GET("search/collection")
//    fun searchCollection(
//        @Query("api_key") apiKey: String,
//        @Query("query") query: String
//    ): Call<CollectionSearchResponse>
//
//    @GET("search/company")
//    fun searchCompany(
//        @Query("api_key") apiKey: String,
//        @Query("query") query: String
//    ): Call<CompanySearchResponse>
}