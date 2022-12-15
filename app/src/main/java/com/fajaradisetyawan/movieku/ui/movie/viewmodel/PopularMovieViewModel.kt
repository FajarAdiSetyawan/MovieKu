/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:39
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.movie.viewmodel

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