/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.remote.endpoint.TrendingApi
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.trending.TrendingResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendingRepository @Inject constructor(private val trendingApi: TrendingApi) {

    fun getTrendingAllDay(): MutableLiveData<TrendingResponse?> {
        val liveData = MutableLiveData<TrendingResponse?>()
        trendingApi.getTrendingAllDay(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TrendingResponse> {
                override fun onResponse(
                    call: Call<TrendingResponse>,
                    response: Response<TrendingResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TrendingResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingAllDay: ${t.message}")
                }
            })
        return liveData
    }

    fun getTrendingAllWeek(): MutableLiveData<TrendingResponse?> {
        val liveData = MutableLiveData<TrendingResponse?>()
        trendingApi.getTrendingAllWeek(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TrendingResponse> {
                override fun onResponse(
                    call: Call<TrendingResponse>,
                    response: Response<TrendingResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TrendingResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingAllWeek: ${t.message}")
                }
            })
        return liveData
    }

    fun getTrendingMovieDay(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        trendingApi.getTrendingMovieDay(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingMovieDay: ${t.message}")
                }
            })
        return liveData
    }

    fun getTrendingMovieWeek(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        trendingApi.getTrendingMovieWeek(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingMovieWeek: ${t.message}")
                }
            })
        return liveData
    }

    fun getTrendingTvShowDay(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        trendingApi.getTrendingTvShowDay(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingTvDay: ${t.message}")
                }
            })
        return liveData
    }

    fun getTrendingTvShowWeek(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        trendingApi.getTrendingTvShowWeek(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful){
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrendingTvWeek: ${t.message}")
                }
            })
        return liveData
    }
}