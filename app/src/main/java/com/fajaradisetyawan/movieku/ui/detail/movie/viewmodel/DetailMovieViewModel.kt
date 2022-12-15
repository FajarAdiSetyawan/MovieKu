/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.ui.detail.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieDetailResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieVideoResponse
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    lateinit var movieDetail: MutableLiveData<MovieDetailResponse?>
    lateinit var movieCredit: MutableLiveData<CreditResponse?>
    lateinit var movieRecommendation: MutableLiveData<MovieResponse?>
    lateinit var movieKeyword: MutableLiveData<KeywordResponse?>
    lateinit var movieExternalIds: MutableLiveData<ExternalIds?>
    lateinit var movieTrailer: MutableLiveData<MovieVideoResponse?>


    fun getDetailMovie(id: Int){
        movieDetail = repository.getDetailMovie(id)
    }

    fun getCreditMovie(id: Int){
        movieCredit = repository.getCreditMovie(id)
    }

    fun getRecommendationMovie(id: Int){
        movieRecommendation = repository.getRecommendationMovie(id)
    }

    fun getKeywordMovie(id: Int){
        movieKeyword = repository.getKeywordMovie(id)
    }

    fun getExternalIds(id: Int){
        movieExternalIds = repository.getExternalIdsMovie(id)
    }

    fun getTrailerMovie(id: Int){
        movieTrailer = repository.getTrailerMovie(id)
    }
}