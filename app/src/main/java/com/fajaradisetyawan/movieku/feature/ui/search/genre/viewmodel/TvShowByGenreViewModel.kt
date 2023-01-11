/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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