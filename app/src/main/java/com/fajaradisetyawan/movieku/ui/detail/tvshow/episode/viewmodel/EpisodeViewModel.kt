/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:42:12
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.tvshow.episode.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val repository: TvShowRepository
) : ViewModel() {

    private val idTvShow = MutableLiveData<Int>()
    private val idSeason = MutableLiveData<Int>()
    private val idEpisode = MutableLiveData<Int>()


}