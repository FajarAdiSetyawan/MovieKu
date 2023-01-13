/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.keyword.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieByKeywordViewModel @Inject constructor(
    repository: MovieRepository
): ViewModel() {
    private val idKeyword = MutableLiveData<String>()
    val movies = idKeyword.switchMap { keywordId ->
        if (keywordId.isNotEmpty()){
            repository.getMovieByKeyword(keywordId).cachedIn(viewModelScope)
        }else{
            repository.getMovieByKeyword(keywordId).cachedIn(viewModelScope)
        }
    }

    fun getMovieByKeyword(keywordId: String){
        idKeyword.postValue(keywordId)
    }
}