/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.people.TvCreditPeopleResponse
import com.fajaradisetyawan.movieku.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowPeopleViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {
    lateinit var tvShowPeople: MutableLiveData<TvCreditPeopleResponse?>

    fun getTvShowPeople(idPeople: Int){
        tvShowPeople = repository.getTvCreditPeople(idPeople)
    }
}