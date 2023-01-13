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
import com.fajaradisetyawan.movieku.data.model.NetworkDetail
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import com.fajaradisetyawan.movieku.data.pagination.tv.*
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.*
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(private val tvShowApi: TvShowApi) {

    fun getPopularTvShow(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        tvShowApi.getPopularTvShow(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailurePopularTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getOnTheAirTvShow(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        tvShowApi.getOnTheAirTvShow(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureOnTheAirTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getAiringTodayTvShow(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        tvShowApi.getAiringTodayTvShow(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureAiringTodayTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getTopRatedTvShow(): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        tvShowApi.getTopRatedTvShow(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTopRatedTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getDetailTvShow(idTv: Int): MutableLiveData<TvShowDetail?> {
        val liveData = MutableLiveData<TvShowDetail?>()
        tvShowApi.getDetailTvShow(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvShowDetail> {
                override fun onResponse(
                    call: Call<TvShowDetail>,
                    response: Response<TvShowDetail>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvShowDetail>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getRecommendationTvShow(idTv: Int): MutableLiveData<TvResponse?> {
        val liveData = MutableLiveData<TvResponse?>()
        tvShowApi.getRecommendationTvShow(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvResponse> {
                override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureRecommendationTvShow: ${t.message}")
                }
            })
        return liveData
    }

    fun getKeywordTvShow(idTv: Int): MutableLiveData<KeywordResponse?> {
        val liveData = MutableLiveData<KeywordResponse?>()
        tvShowApi.getKeywordTvShow(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<KeywordResponse> {
                override fun onResponse(
                    call: Call<KeywordResponse>,
                    response: Response<KeywordResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureKeywordTv: ${t.message}")
                }
            })
        return liveData
    }

    fun getExternalIdTv(idTv: Int): MutableLiveData<ExternalIds?> {
        val liveData = MutableLiveData<ExternalIds?>()
        tvShowApi.getExternalLinkTvShow(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<ExternalIds> {
                override fun onResponse(call: Call<ExternalIds>, response: Response<ExternalIds>) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ExternalIds>, t: Throwable) {
                    Log.d("TAG", "onFailureExternalIdTv: ${t.message}")
                }

            })
        return liveData
    }

    fun getTrailerTv(idTv: Int): MutableLiveData<TvShowVideoResponse?> {
        val liveData = MutableLiveData<TvShowVideoResponse?>()
        tvShowApi.getTvShowVideos(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvShowVideoResponse> {
                override fun onResponse(
                    call: Call<TvShowVideoResponse>,
                    response: Response<TvShowVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvShowVideoResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrailerTv: ${t.message}")
                }

            })
        return liveData
    }

    fun getCreditsTv(idTv: Int): MutableLiveData<CreditResponse?> {
        val liveData = MutableLiveData<CreditResponse?>()
        tvShowApi.getTvShowCredits(idTv, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<CreditResponse> {
                override fun onResponse(
                    call: Call<CreditResponse>,
                    response: Response<CreditResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<CreditResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureCreditsTv: ${t.message}")
                }
            })
        return liveData
    }

    fun getDetailNetwork(idNetwork: Int): MutableLiveData<NetworkDetail?> {
        val liveData = MutableLiveData<NetworkDetail?>()
        tvShowApi.getDetailNetwork(idNetwork, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<NetworkDetail> {
                override fun onResponse(
                    call: Call<NetworkDetail>,
                    response: Response<NetworkDetail>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<NetworkDetail>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailNetwork: ${t.message}")
                }
            })
        return liveData
    }

    fun getDetailEpisode(idTv: Int, idSeason: Int, idEpisode: Int): MutableLiveData<TvEpisodeResponse?> {
        val liveData = MutableLiveData<TvEpisodeResponse?>()
        tvShowApi.getEpisodeTvShow(idTv, idSeason, idEpisode, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<TvEpisodeResponse> {
                override fun onResponse(
                    call: Call<TvEpisodeResponse>,
                    response: Response<TvEpisodeResponse>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<TvEpisodeResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailEpisode: ${t.message}")
                }
            })
        return liveData
    }

    fun getTvShowByNetwork(idNetwork: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TvByNetworkPagingSource(tvShowApi, idNetwork) }
        ).liveData


    fun getTvByGenre(idGenre: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TvByGenrePagingSource(tvShowApi, idGenre) }
        ).liveData

    fun getTvByKeyword(idKeyword: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TvByKeywordPagingSource(tvShowApi, idKeyword) }
        ).liveData


    fun getAllSeason(idTv: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SeasonPagingSource(tvShowApi, idTv) }
        ).liveData

    fun getAllEpisode(idTv: Int, idSeason: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EpisodePagingSource(tvShowApi, idTv, idSeason) }
        ).liveData


    fun getCastTvShow(idTv: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CastTvShowPagingSource(tvShowApi, idTv) }
        ).liveData

    fun getCrewTvShow(idTv: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CrewTvShowPagingSource(tvShowApi, idTv) }
        ).liveData

    fun getCastEpisode(idTv: Int, idSeason: Int, idEpisode: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CastEpisodePagingSource(tvShowApi, idTv, idSeason, idEpisode) }
        ).liveData

    fun getCrewEpisode(idTv: Int, idSeason: Int, idEpisode: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CrewEpisodePagingSource(tvShowApi, idTv, idSeason, idEpisode) }
        ).liveData

    fun getGuestStarEpisode(idTv: Int, idSeason: Int, idEpisode: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GuestStarPagingSource(tvShowApi, idTv, idSeason, idEpisode) }
        ).liveData

}