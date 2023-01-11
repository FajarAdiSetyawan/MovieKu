/*
 * Created by Fajar Adi Setyawan on 6/1/2023 - 10:45:32
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fajaradisetyawan.movieku.repository.favorite.FavoriteMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieFavViewModel @Inject constructor(
    repository: FavoriteMovieRepository
) : ViewModel() {
    val movies = repository.getFavoriteMovies()
}