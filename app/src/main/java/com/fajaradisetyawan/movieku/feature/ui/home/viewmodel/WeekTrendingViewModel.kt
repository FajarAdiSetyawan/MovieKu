/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.trending.TrendingResponse
import com.fajaradisetyawan.movieku.repository.TrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeekTrendingViewModel@Inject constructor(
    private val repository: TrendingRepository,
) : ViewModel() {

    lateinit var trending: MutableLiveData<TrendingResponse?>

    init {
        getTrendingWeek()
    }

    private fun getTrendingWeek() {
        trending = repository.getTrendingAllWeek()
    }
}