/*
 * Created by Fajar Adi Setyawan on 22/12/2022 - 12:17:59
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.allPeople.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllPeopleMovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    lateinit var movieCredit: MutableLiveData<CreditResponse?>

    fun getCreditMovie(id: Int){
        movieCredit = repository.getCreditMovie(id)
    }

}