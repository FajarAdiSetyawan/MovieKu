/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.repository.PersonRepository
import com.fajaradisetyawan.movieku.repository.favorite.FavoritePeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPeopleViewModel @Inject constructor(
    private val repository: PersonRepository,
    private val favRepository: FavoritePeopleRepository
) : ViewModel() {
    lateinit var detailPeople: MutableLiveData<PeopleDetail?>
    lateinit var externalPeople: MutableLiveData<ExternalIds?>

    fun getDetailPeople(idPeople: Int) {
        detailPeople = repository.getDetailPeople(idPeople)
    }

    fun getExternalPeople(idPeople: Int) {
        externalPeople = repository.getExternalIdPeople(idPeople)
    }

    // Favorite
    fun addToFavorite(peopleDetail: PeopleDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            favRepository.addFavorite(peopleDetail)
        }
    }

    fun removeFromFavorite(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            favRepository.deleteFavorite(id)
        }
    }

    suspend fun checkPeople(id: Int) = favRepository.getPeopleById(id)
}