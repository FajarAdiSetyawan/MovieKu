/*
 * Created by Fajar Adi Setyawan on 22/12/2022 - 12:29:3
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.tv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllPeopleTvShowViewModel @Inject constructor(
    private val repository: TvShowRepository
): ViewModel() {
    lateinit var tvCredits: MutableLiveData<CreditResponse?>

    fun getCreditsTv(id: Int){
        tvCredits = repository.getCreditsTv(id)
    }
}