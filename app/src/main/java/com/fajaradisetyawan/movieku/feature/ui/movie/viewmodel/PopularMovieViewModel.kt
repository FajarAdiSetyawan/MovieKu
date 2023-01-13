/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    lateinit var moviePopular: MutableLiveData<MovieResponse?>

    init {
        getMoviePopular()
    }

    private fun getMoviePopular(){
        moviePopular = repository.getPopularMovie()
    }
}