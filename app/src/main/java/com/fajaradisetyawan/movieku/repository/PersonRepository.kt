/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.data.remote.endpoint.PeopleApi
import com.fajaradisetyawan.movieku.data.pagination.people.PersonPopularPagingSource
import com.fajaradisetyawan.movieku.data.remote.response.people.MovieCreditPeopleResponse
import com.fajaradisetyawan.movieku.data.remote.response.people.TvCreditPeopleResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(private val peopleApi: PeopleApi){
    fun getPopularPeople() =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = {PersonPopularPagingSource(peopleApi)}
        ).liveData

    fun getDetailPeople(idPeople: Int): MutableLiveData<PeopleDetail?> {
        val liveData = MutableLiveData<PeopleDetail?>()
        peopleApi.getDetailPeople(idPeople, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<PeopleDetail> {
                override fun onResponse(
                    call: Call<PeopleDetail>,
                    response: Response<PeopleDetail>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<PeopleDetail>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailPeople: ${t.message}")
                }
            })
        return liveData
    }

    fun getMovieCreditPeople(idPeople: Int): MutableLiveData<MovieCreditPeopleResponse?>{
        val liveData = MutableLiveData<MovieCreditPeopleResponse?>()
        peopleApi.getMoviePeople(idPeople, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieCreditPeopleResponse> {
                override fun onResponse(
                    call: Call<MovieCreditPeopleResponse>,
                    response: Response<MovieCreditPeopleResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<MovieCreditPeopleResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureMovieCreditPeople: ${t.message}")
                }
            })
        return liveData
    }

    fun getTvCreditPeople(idPeople: Int): MutableLiveData<TvCreditPeopleResponse?>{
        val liveData = MutableLiveData<TvCreditPeopleResponse?>()
        peopleApi.getTvPeople(idPeople, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvCreditPeopleResponse> {
                override fun onResponse(
                    call: Call<TvCreditPeopleResponse>,
                    response: Response<TvCreditPeopleResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvCreditPeopleResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTvCreditPeople: ${t.message}")
                }
            })
        return liveData
    }

    fun getExternalIdPeople(idPeople: Int): MutableLiveData<ExternalIds?>{
        val liveData = MutableLiveData<ExternalIds?>()
        peopleApi.getExternalIdsPeople(idPeople, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<ExternalIds> {
                override fun onResponse(
                    call: Call<ExternalIds>,
                    response: Response<ExternalIds>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ExternalIds>, t: Throwable) {
                    Log.d("TAG", "onFailureExternalIdPeople: ${t.message}")
                }
            })
        return liveData
    }

}