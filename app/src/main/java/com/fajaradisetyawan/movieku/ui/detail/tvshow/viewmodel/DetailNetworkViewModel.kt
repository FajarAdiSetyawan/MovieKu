/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 12:2:6
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.tvshow.viewmodel

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
            repository.getTvShowByNetwork(networkId)
        }else{
            repository.getTvShowByNetwork("").cachedIn(viewModelScope)
        }
    }

    fun getTvByNetwork(networkId: String){
        idNetwork.postValue(networkId)
    }

}