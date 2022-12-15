/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.people.detail.viewmodel

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