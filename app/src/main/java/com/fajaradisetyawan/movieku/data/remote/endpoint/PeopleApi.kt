/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.endpoint

import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.data.remote.response.people.MovieCreditPeopleResponse
import com.fajaradisetyawan.movieku.data.remote.response.people.PeoplePopularResponse
import com.fajaradisetyawan.movieku.data.remote.response.people.TvCreditPeopleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {
    @GET("person/popular")
    suspend fun getPeoplePopular(
        @Query("api_key") apiKey: String,
        @Query("page") position: Int
    ): PeoplePopularResponse

    @GET("person/{id}")
    fun getDetailPeople(
        @Path("id") personId: Int,
        @Query("api_key") apiKey: String
    ): Call<PeopleDetail>

    @GET("person/{id}/movie_credits")
    fun getMoviePeople(
        @Path("id") personId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieCreditPeopleResponse>

    @GET("person/{id}/tv_credits")
    fun getTvPeople(
        @Path("id") personId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvCreditPeopleResponse>

    @GET("person/{id}/external_ids")
    fun getExternalIdsPeople(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<ExternalIds>

}