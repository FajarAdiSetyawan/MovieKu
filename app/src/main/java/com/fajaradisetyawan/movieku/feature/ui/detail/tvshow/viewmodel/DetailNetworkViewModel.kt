/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.tvshow.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.data.model.NetworkDetail
import com.fajaradisetyawan.movieku.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailNetworkViewModel @Inject constructor(
    private val repository: TvShowRepository
): ViewModel() {
    lateinit var detail: MutableLiveData<NetworkDetail?>

    fun getDetailNetwork(id: Int){
        detail = repository.getDetailNetwork(id)
    }

    private val idNetwork = MutableLiveData<String>()
    val tvShow = idNetwork.switchMap { networkId ->
        if (networkId.isNotEmpty()){
            repository.getTvShowByNetwork(networkId).cachedIn(viewModelScope)
        }else{
            repository.getTvShowByNetwork("").cachedIn(viewModelScope)
        }
    }

    fun getTvByNetwork(networkId: String){
        idNetwork.postValue(networkId)
    }

}