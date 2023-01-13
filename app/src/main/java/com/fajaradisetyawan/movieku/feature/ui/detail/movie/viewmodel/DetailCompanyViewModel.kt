/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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