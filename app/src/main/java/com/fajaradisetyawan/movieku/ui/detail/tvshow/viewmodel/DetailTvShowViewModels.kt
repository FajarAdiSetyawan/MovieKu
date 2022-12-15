/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvShowDetailResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvShowVideoResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailTvShowViewModels @Inject constructor(
    private val repository: TvShowRepository
): ViewModel() {
    lateinit var tvDetail: MutableLiveData<TvShowDetailResponse?>
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
}