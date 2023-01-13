/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.search.search.tab.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanySearchViewModel @Inject constructor(
    repository: SearchRepository
): ViewModel() {
    private val query = MutableLiveData<String>()
    val company = query.switchMap { query ->
        if (query.isNotEmpty()){
            repository.getSearchCompany(query).cachedIn(viewModelScope)
        }else{
            repository.getSearchCompany("").cachedIn(viewModelScope)
        }
    }

    fun getSearchCompany(companyQuery: String){
        query.postValue(companyQuery)
    }
}