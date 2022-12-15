/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.model.CompaniesDetail
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.CollectionResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieDetailResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieVideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("discover/movie")
    fun getMovieReleasedToday(
        @Query("api_key") apiKey: String,
        @Query("primary_release.gte") dateToday: String,
        @Query("primary_release.lte") dateToday2: String
    ): Call<MovieResponse>

    @GET("movie/{id}/videos")
    fun getMovieVideos(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieVideoResponse>

    @GET("movie/{id}/credits")
    suspend fun getMovieCreditsPaging(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): CreditResponse

    @GET("movie/{id}/credits")
    fun getMovieCredits(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<CreditResponse>

    @GET("movie/{id}/recommendations")
    fun getRecommendationMovie(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovie(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET("movie/{id}")
    fun getDetailMovie(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>


    @GET("movie/{id}/keywords")
    fun getKeywordMovie(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<KeywordResponse>

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String,
        @Query("page") position: Int
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getMovieByKeyword(
        @Query("api_key") apiKey: String,
        @Query("with_keywords") keywordId: String,
        @Query("page") position: Int
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getMovieByCompanies(
        @Query("api_key") apiKey: String,
        @Query("with_companies") companiesId: String,
        @Query("page") position: Int
    ): MovieResponse

    @GET("company/{id}")
    fun getDetailCompanies(
        @Path("id") companiesId: Int,
        @Query("api_key") apiKey: String
    ): Call<CompaniesDetail>

    @GET("movie/{id}/external_ids")
    fun getExternalIdsMovie(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<ExternalIds>

    @GET("collection/{id}")
    fun getCollectionMovie(
        @Path("id") collectionId: Int,
        @Query("api_key") apiKey: String
    ): Call<CollectionResponse>

}