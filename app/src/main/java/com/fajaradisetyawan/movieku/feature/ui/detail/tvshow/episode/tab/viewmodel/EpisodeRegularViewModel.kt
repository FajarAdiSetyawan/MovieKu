/*
 * Created by Fajar Adi Setyawan on 16/1/2023 - 12:20:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.tab.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeRegularViewModel @Inject constructor(
    private val repository: TvShowRepository,
) : ViewModel() {

    private val selected = MutableLiveData<Episode>()

    fun getCreditsEpisode(episode: Episode?){
        selected.postValue(episode!!)
    }

    val cast = selected.switchMap { episode ->
        if (episode != null) {
            repository.getCastEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber).cachedIn(viewModelScope)
        }else{
            repository.getCastEpisode(0, 0, 0).cachedIn(viewModelScope)
        }
    }
}