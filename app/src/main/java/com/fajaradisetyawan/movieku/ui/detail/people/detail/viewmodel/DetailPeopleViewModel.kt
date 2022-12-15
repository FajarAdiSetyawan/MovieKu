/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailPeopleViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {
    lateinit var detailPeople: MutableLiveData<PeopleDetail?>
    lateinit var externalPeople: MutableLiveData<ExternalIds?>

    fun getDetailPeople(idPeople: Int) {
        detailPeople = repository.getDetailPeople(idPeople)
    }

    fun getExternalPeople(idPeople: Int) {
        externalPeople = repository.getExternalIdPeople(idPeople)
    }
}