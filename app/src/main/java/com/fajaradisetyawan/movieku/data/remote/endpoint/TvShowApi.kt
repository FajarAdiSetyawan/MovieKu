/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.model.NetworkDetail
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowApi {

    @GET("discover/tv")
    fun discoverTv(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/{id}/videos")
    fun getTvShowVideos(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowVideoResponse>

    @GET("tv/{id}/credits")
    suspend fun getTvShowCreditsPaging(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): CreditResponse

    @GET("tv/{id}/credits")
    fun getTvShowCredits(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<CreditResponse>

    @GET("tv/{id}/recommendations")
    fun getRecommendationTvShow(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShow(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/popular")
    fun getPopularTvShow(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/airing_today")
    fun getAiringTodayTvShow(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShow(
        @Query("api_key") apiKey: String
    ): Call<TvResponse>

    @GET("tv/{id}")
    fun getDetailTvShow(
        @Path("id") tvShowId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowDetailResponse>

    @GET("tv/{id}")
    suspend fun getAllSeasonTvShow(
        @Path("id") tvShowId: Int,
        @Query("api_key") apiKey: String
    ): TvShowDetailResponse

    @GET("tv/{id}/keywords")
    fun getKeywordTvShow(
        @Path("id") tvShowId: Int,
        @Query("api_key") apiKey: String
    ): Call<KeywordResponse>

    @GET("discover/tv")
    suspend fun getTvShowByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String,
        @Query("page") position: Int
    ): TvResponse

    @GET("discover/tv")
    suspend fun getTvShowByKeyword(
        @Query("api_key") apiKey: String,
        @Query("with_keywords") keywordId: String,
        @Query("page") position: Int
    ): TvResponse

    @GET("network/{id}")
    fun getDetailNetwork(
        @Path("id") networksId: Int,
        @Query("api_key") apiKey: String,
    ): Call<NetworkDetail>

    @GET("discover/tv")
    suspend fun getTvShowByNetwork(
        @Query("api_key") apiKey: String,
        @Query("with_networks") networksId: String,
        @Query("page") position: Int
    ): TvResponse

    @GET("tv/{id}/external_ids")
    fun getExternalLinkTvShow(
        @Path("id") tvShowId: Int,
        @Query("api_key") apiKey: String,
    ): Call<ExternalIds>

    @GET("tv/{id}/season/{season}")
    fun getSeasonTvShow(
        @Path("id") tvShowId: Int,
        @Path("season") seasonId: Int,
        @Query("api_key") apiKey: String,
    ): Call<Seasons>

    @GET("tv/{id}/season/{season}")
    suspend fun getAllEpisodeTvShow(
        @Path("id") tvShowId: Int,
        @Path("season") seasonId: Int,
        @Query("api_key") apiKey: String,
    ): TvSeasonResponse

    @GET("tv/{idTv}/season/{idSeason}/episode/{idEpisode}")
    fun getEpisodeTvShow(
        @Path("idTv") tvShowId: Int,
        @Path("idSeason") seasonId: Int,
        @Path("idEpisode") episodeId: Int,
        @Query("api_key") apiKey: String,
    ): Call<TvEpisodeResponse>

    @GET("tv/{idTv}/season/{idSeason}/episode/{idEpisode}/credits")
    suspend fun getCreditEpisodeTvShow(
        @Path("idTv") tvShowId: Int,
        @Path("idSeason") seasonId: Int,
        @Path("idEpisode") episodeId: Int,
        @Query("api_key") apiKey: String,
    ): TvEpisodeCreditResponse
}