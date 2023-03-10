/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.fajaradisetyawan.movieku.repository.favorite.FavoriteTvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowFavViewModel @Inject constructor(
    repository: FavoriteTvRepository
): ViewModel(){
    private val currentQuery = MutableLiveData<String>()
    val tvShow = currentQuery.switchMap { query ->
        if (query.isNotEmpty()){
            repository.searchTvShow(query).asLiveData()
        }else{
            repository.getFavoriteTvShow().asLiveData()
        }
    }

    fun searchTvFav(query: String) {
        currentQuery.value = query
    }
}