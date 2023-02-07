/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:58:49
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.fajaradisetyawan.movieku.repository.watchlist.WatchListMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchListMovieViewModel @Inject constructor(
    private val repository: WatchListMovieRepository
): ViewModel(){
    private val currentQuery = MutableLiveData<String>()
    val movie = currentQuery.switchMap { query ->
        if (query.isNotEmpty()){
            repository.searchMovie(query).asLiveData()
        }else{
            repository.getWatchListMovies().asLiveData()
        }
    }

    fun searchMovies(query: String) {
        currentQuery.value = query
    }
}