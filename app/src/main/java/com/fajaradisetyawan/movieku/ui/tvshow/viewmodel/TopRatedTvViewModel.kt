/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.tvshow.TvResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedTvViewModel @Inject constructor(
    val repository: TvShowRepository
) : ViewModel() {
    lateinit var topRated: MutableLiveData<TvResponse?>

    init {
        getTopRated()
    }

    private fun getTopRated(){
        topRated = repository.getTopRatedTvShow()
    }
}