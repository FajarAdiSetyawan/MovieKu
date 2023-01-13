/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCastMovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    private val idMovie = MutableLiveData<Int>()

    val cast = idMovie.switchMap { movieId ->
        if (movieId != 0){
            repository.getCastMovie(movieId).cachedIn(viewModelScope)
        }else{
            repository.getCastMovie(0).cachedIn(viewModelScope)
        }
    }

    fun getMovieCredits(movieId: Int){
        idMovie.postValue(movieId)
    }
}