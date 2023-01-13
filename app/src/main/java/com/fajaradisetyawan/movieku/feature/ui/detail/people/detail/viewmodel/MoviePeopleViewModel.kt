/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:45
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.detail.people.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.remote.response.people.MovieCreditPeopleResponse
import com.fajaradisetyawan.movieku.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviePeopleViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    lateinit var moviePeople: MutableLiveData<MovieCreditPeopleResponse?>

    fun getMoviePeople(idPeople: Int){
        moviePeople = repository.getMovieCreditPeople(idPeople)
    }
}