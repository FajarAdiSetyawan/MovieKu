/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.keyword.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowByKeywordViewModel @Inject constructor(
    repository: TvShowRepository,
): ViewModel() {

    private val idKeyword = MutableLiveData<String>()
    val tvShow = idKeyword.switchMap { keywordId ->
        if (keywordId.isNotEmpty()){
            repository.getTvByKeyword(keywordId).cachedIn(viewModelScope)
        }else{
            repository.getTvByKeyword("").cachedIn(viewModelScope)
        }
    }

    fun getTvByKeyword(keywordId: String){
        idKeyword.postValue(keywordId)
    }
}