/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvShowVideoResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import com.fajaradisetyawan.movieku.repository.favorite.FavoriteTvRepository
import com.fajaradisetyawan.movieku.repository.watchlist.WatchListTvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTvShowViewModels @Inject constructor(
    private val repository: TvShowRepository,
    private val repositoryFavorite: FavoriteTvRepository,
    private val repositoryWatchList: WatchListTvRepository,
): ViewModel() {
    lateinit var tvDetail: MutableLiveData<TvShowDetail?>
    lateinit var tvKeyword: MutableLiveData<KeywordResponse?>
    lateinit var tvRecommendation: MutableLiveData<TvResponse?>
    lateinit var tvCredits: MutableLiveData<CreditResponse?>
    lateinit var tvExternalIds: MutableLiveData<ExternalIds?>
    lateinit var tvTrailer: MutableLiveData<TvShowVideoResponse?>

    fun getDetailTvShow(id: Int){
        tvDetail = repository.getDetailTvShow(id)
    }

    fun getKeywordTvShow(id: Int){
        tvKeyword = repository.getKeywordTvShow(id)
    }

    fun getRecommendationTv(id: Int){
        tvRecommendation = repository.getRecommendationTvShow(id)
    }

    fun getCreditsTv(id: Int){
        tvCredits = repository.getCreditsTv(id)
    }

    fun getExternalIds(id: Int){
        tvExternalIds = repository.getExternalIdTv(id)
    }

    fun getTrailerTv(id: Int){
        tvTrailer = repository.getTrailerTv(id)
    }

    // Favorite
    fun addToFavorite(tvShowDetail: TvShowDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.addFavorite(tvShowDetail)
        }
    }

    fun removeFromFavorite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.deleteFavorite(id)
        }
    }

    suspend fun checkTvShow(id: Int) = repositoryFavorite.getTvShowId(id)

    // WatchList
    fun addToWatchList(tvShowDetail: TvShowDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryWatchList.addWatchList(tvShowDetail)
        }
    }

    fun removeWatchLis(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryWatchList.deleteWatchList(id)
        }
    }

    suspend fun checkTvShowWatchList(id: Int) = repositoryWatchList.getTvShowId(id)
}