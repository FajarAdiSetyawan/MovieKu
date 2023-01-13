/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.season.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel @Inject constructor(
    private val repository: TvShowRepository
) : ViewModel() {

    private val idTvShow = MutableLiveData<Int>()
    private val idSeason = MutableLiveData<Int>()

//     the Pair values are nullable as getting "liveData.value" can be null
    private val combinedValues = MediatorLiveData<Pair<Int?, Int?>>().apply {
        addSource(idTvShow) {
            value = Pair(it, idSeason.value)
        }
        addSource(idSeason) {
            value = Pair(idTvShow.value, it)
        }
    }

    val results = Transformations.switchMap(combinedValues) { pair ->
        val idTvShow = pair.first
        val idSeason = pair.second
        if (idTvShow != null && idSeason != null) {
            repository.getAllEpisode(idTvShow, idSeason).cachedIn(viewModelScope)
        } else {
            repository.getAllEpisode(0, 0)
        }
    }

    fun getAllEpisode(tvShowId: Int, seasonId: Int) {
        idTvShow.postValue(tvShowId)
        idSeason.postValue(seasonId)
    }

}