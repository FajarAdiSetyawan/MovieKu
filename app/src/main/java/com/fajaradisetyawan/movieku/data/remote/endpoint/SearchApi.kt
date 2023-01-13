/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.search.SearchCollectionResponse
import com.fajaradisetyawan.movieku.data.remote.response.search.SearchCompanyResponse
import com.fajaradisetyawan.movieku.data.remote.response.search.SearchKeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.search.SearchPeopleResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): MovieResponse

    @GET("search/tv")
    suspend fun searchTv(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): TvResponse

    @GET("search/person")
    suspend fun searchPeople(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): SearchPeopleResponse

    @GET("search/keyword")
    suspend fun searchKeyword(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): SearchKeywordResponse

    @GET("search/collection")
    suspend fun searchCollection(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): SearchCollectionResponse

    @GET("search/company")
    suspend fun searchCompany(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") position: Int
    ): SearchCompanyResponse
}