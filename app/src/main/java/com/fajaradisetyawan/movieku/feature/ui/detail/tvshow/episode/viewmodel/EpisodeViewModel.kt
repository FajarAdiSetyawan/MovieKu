/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:42:12
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvEpisodeResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val repository: TvShowRepository,
) : ViewModel() {

    private val selected = MutableLiveData<Episode>()
    lateinit var detail: MutableLiveData<TvEpisodeResponse?>

    fun getCreditsEpisode(episode: Episode?){
        selected.postValue(episode!!)
    }

    val crew = selected.switchMap { episode ->
        if (episode != null) {
            repository.getCrewEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber).cachedIn(viewModelScope)
        }else{
            repository.getCrewEpisode(0, 0, 0).cachedIn(viewModelScope)
        }
    }

    val cast = selected.switchMap { episode ->
        if (episode != null) {
            repository.getCastEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber).cachedIn(viewModelScope)
        }else{
            repository.getCastEpisode(0, 0, 0).cachedIn(viewModelScope)
        }
    }

    val guestStar = selected.switchMap { episode ->
        if (episode != null) {
            repository.getGuestStarEpisode(episode.idTvShow, episode.seasonNumber, episode.episodeNumber).cachedIn(viewModelScope)
        }else{
            repository.getGuestStarEpisode(0, 0, 0).cachedIn(viewModelScope)
        }
    }

    fun getDetailEpisode(idTv: Int, idSeason: Int, idEpisode: Int){
        detail = repository.getDetailEpisode(idTv, idSeason, idEpisode)
    }
}