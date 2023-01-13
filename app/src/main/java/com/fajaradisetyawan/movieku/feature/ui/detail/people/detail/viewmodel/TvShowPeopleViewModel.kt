/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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