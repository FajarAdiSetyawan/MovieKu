/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 12:0:13
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.movie.viewmodel

import androidx.lifecycle.*
import com.fajaradisetyawan.movieku.data.model.CompaniesDetail
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailCompanyViewModel @Inject constructor(
    val repository: MovieRepository,
    state: SavedStateHandle
) : ViewModel() {
    lateinit var detail: MutableLiveData<CompaniesDetail?>

    fun getDetailCompany(id: Int) {
        detail = repository.getDetailCompanies(id)
    }

    private val idCompany = MutableLiveData<String>()

    val movies = idCompany.switchMap { companyId ->
        if (companyId.isNotEmpty()) {
            repository.getMovieByCompanies(companyId)
        } else {
            repository.getMovieByCompanies("")
        }
    }

    fun getMovieByCompany(companyId: String) {
        idCompany.postValue(companyId)
    }

}