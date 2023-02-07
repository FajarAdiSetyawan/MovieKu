/*
 * Created by Fajar Adi Setyawan on 7/2/2023 - 11:58:58
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.watchlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.fajaradisetyawan.movieku.repository.watchlist.WatchListTvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchListTvViewModel @Inject constructor(
    private val repository: WatchListTvRepository
): ViewModel(){
    private val currentQuery = MutableLiveData<String>()
    val tvShow = currentQuery.switchMap { query ->
        if (query.isNotEmpty()){
            repository.searchTvShow(query).asLiveData()
        }else{
            repository.getWatchListTvShow().asLiveData()
        }
    }

    fun searchTvWatchList(query: String) {
        currentQuery.value = query
    }
}