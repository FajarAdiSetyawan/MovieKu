/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.allPeople.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllPeopleMovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    private val idMovie = MutableLiveData<Int>()

    val crew = idMovie.switchMap { movieId ->
        if (movieId != 0){
            repository.getCrewMovie(movieId).cachedIn(viewModelScope)
        }else{
            repository.getCrewMovie(0).cachedIn(viewModelScope)
        }
    }

    val cast = idMovie.switchMap { movieId ->
        if (movieId != 0){
            repository.getCastMovie(movieId).cachedIn(viewModelScope)
        }else{
            repository.getCastMovie(0).cachedIn(viewModelScope)
        }
    }

    fun getCreditsMovie(movieId: Int){
        idMovie.postValue(movieId)
    }
}