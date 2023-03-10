/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCastTvViewModel @Inject constructor(
    private val repository: TvShowRepository
): ViewModel() {
    private val idTvShow = MutableLiveData<Int>()

    val cast = idTvShow.switchMap { tvShowId ->
        if (tvShowId != 0){
            repository.getCastTvShow(tvShowId).cachedIn(viewModelScope)
        }else{
            repository.getCastTvShow(0).cachedIn(viewModelScope)
        }
    }

    fun getTvCredits(tvShowId: Int){
        idTvShow.postValue(tvShowId)
    }
}