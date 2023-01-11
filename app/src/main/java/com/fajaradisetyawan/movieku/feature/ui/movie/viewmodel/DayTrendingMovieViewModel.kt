/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.repository.TrendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayTrendingMovieViewModel @Inject constructor(
    private val repository: TrendingRepository
) : ViewModel() {

    lateinit var trending: MutableLiveData<MovieResponse?>

    init {
        getTrendingDay()
    }

    private fun getTrendingDay() {
        trending = repository.getTrendingMovieDay()
    }
}