/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.search.keyword.viewmodel

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
            repository.getMovieByKeyword(keywordId)
        }else{
            repository.getMovieByKeyword(keywordId).cachedIn(viewModelScope)
        }
    }

    fun getMovieByKeyword(keywordId: String){
        idKeyword.postValue(keywordId)
    }
}