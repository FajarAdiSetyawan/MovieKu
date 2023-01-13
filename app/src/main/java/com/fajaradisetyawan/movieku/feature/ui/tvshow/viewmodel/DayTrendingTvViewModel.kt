/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import com.fajaradisetyawan.movieku.repository.TrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayTrendingTvViewModel @Inject constructor(
    val repository: TrendingRepository
) : ViewModel() {
    lateinit var trending: MutableLiveData<TvResponse?>

    init {
        getTrending()
    }

    private fun getTrending(){
        trending = repository.getTrendingTvShowDay()
    }
}