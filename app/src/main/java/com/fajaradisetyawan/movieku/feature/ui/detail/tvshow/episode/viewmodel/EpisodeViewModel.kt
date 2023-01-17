/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.episode.viewmodel

import androidx.lifecycle.*
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvEpisodeCreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvEpisodeResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val repository: TvShowRepository,
) : ViewModel() {
    lateinit var detail: MutableLiveData<TvEpisodeResponse?>
    lateinit var credit: MutableLiveData<TvEpisodeCreditResponse?>


    fun getDetailEpisode(idTv: Int, idSeason: Int, idEpisode: Int){
        detail = repository.getDetailEpisode(idTv, idSeason, idEpisode)
    }

    fun getCreditEpisode(idTv: Int, idSeason: Int, idEpisode: Int){
        credit = repository.getCreditEpisode(idTv, idSeason, idEpisode)
    }


}