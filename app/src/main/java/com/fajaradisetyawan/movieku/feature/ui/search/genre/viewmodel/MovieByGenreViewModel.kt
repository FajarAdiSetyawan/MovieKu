/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.genre.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieByGenreViewModel @Inject constructor(
    repository: MovieRepository
): ViewModel() {

    private val idGenre = MutableLiveData<String>()
    val movies = idGenre.switchMap { genreId ->
        if (genreId.isNotEmpty()){
            repository.getMovieByGenre(genreId)
        }else{
            repository.getMovieByGenre("").cachedIn(viewModelScope)
        }
    }

    fun getMovieByGenre(genreId: String){
        idGenre.postValue(genreId)
    }

}