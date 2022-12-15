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
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllPeopleTvShowViewModel @Inject constructor(
    private val repository: TvShowRepository
): ViewModel() {
    private val idTvShow = MutableLiveData<Int>()

    val crew = idTvShow.switchMap { tvShowId ->
        if (tvShowId != 0){
            repository.getCrewTvShow(tvShowId).cachedIn(viewModelScope)
        }else{
            repository.getCrewTvShow(0).cachedIn(viewModelScope)
        }
    }

    val cast = idTvShow.switchMap { tvShowId ->
        if (tvShowId != 0){
            repository.getCastTvShow(tvShowId).cachedIn(viewModelScope)
        }else{
            repository.getCastTvShow(0).cachedIn(viewModelScope)
        }
    }

    fun getCreditsTv(tvShowId: Int){
        idTvShow.postValue(tvShowId)
    }
}