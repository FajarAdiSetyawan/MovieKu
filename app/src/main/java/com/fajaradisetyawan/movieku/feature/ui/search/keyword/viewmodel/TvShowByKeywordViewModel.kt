/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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