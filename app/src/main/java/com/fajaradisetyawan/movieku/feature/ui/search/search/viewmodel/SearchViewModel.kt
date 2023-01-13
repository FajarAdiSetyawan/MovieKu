/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.remote.response.trending.TrendingResponse
import com.fajaradisetyawan.movieku.repository.TrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TrendingRepository
): ViewModel() {
    lateinit var trending: MutableLiveData<TrendingResponse?>

    init {
        getTrendingDay()
    }

    private fun getTrendingDay() {
        trending = repository.getTrendingAllDay()
    }
}