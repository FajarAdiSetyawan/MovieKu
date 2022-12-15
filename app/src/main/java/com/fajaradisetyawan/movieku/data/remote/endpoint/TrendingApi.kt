/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.trending.TrendingResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingApi {
    @GET("trending/all/day")
    fun getTrendingAllDay(
        @Query("api_key") apiKey: String
    ): Call<TrendingResponse>

    @GET("trending/all/week")
    fun getTrendingAllWeek(
        @Query("api_key") apiKey: String
    ): Call<TrendingResponse>

    @GET("trending/movie/day")
    fun getTrendingMovieDay(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("trending/movie/week")
    fun getTrendingMovieWeek(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("trending/tv/day")
    fun getTrendingTvShowDay(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("trending/tv/week")
    fun getTrendingTvShowWeek(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>
}