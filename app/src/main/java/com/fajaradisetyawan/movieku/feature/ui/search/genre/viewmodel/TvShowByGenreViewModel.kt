/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.genre.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowByGenreViewModel @Inject constructor(
    repository: TvShowRepository
): ViewModel() {

    private val idGenre = MutableLiveData<String>()
    val tvShow = idGenre.switchMap { genreId ->
        if (genreId.isNotEmpty()){
            repository.getTvByGenre(genreId)
        }else{
            repository.getTvByGenre("").cachedIn(viewModelScope)
        }
    }

    fun getTvByGenre(genreId: String){
        idGenre.postValue(genreId)
    }
}