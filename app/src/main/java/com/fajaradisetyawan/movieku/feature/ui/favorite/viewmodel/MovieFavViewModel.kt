/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.repository.favorite.FavoriteMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieFavViewModel @Inject constructor(
    private val repository: FavoriteMovieRepository,
) : ViewModel() {

    private val currentQuery = MutableLiveData<String>()
    val movie = currentQuery.switchMap { query ->
        if (query.isNotEmpty()){
            repository.searchFavMovie(query).asLiveData()
        }else{
            repository.getFavoriteMovies().asLiveData()
        }
    }

    fun searchMovies(query: String) {
        currentQuery.value = query
    }
}