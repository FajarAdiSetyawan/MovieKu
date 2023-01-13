/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.season.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllSeasonViewModel @Inject constructor(
    private val repository: TvShowRepository
) : ViewModel() {
    private val idTvShow = MutableLiveData<Int>()
    val allSeason = idTvShow.switchMap { tvShowId ->
        if (tvShowId != 0){
            repository.getAllSeason(tvShowId)
        }else{
            repository.getAllSeason(0).cachedIn(viewModelScope)
        }
    }

    fun getAllSeason(tvShowId: Int){
        idTvShow.postValue(tvShowId)
    }
}