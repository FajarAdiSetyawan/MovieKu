/*
 * Created by Fajar Adi Setyawan on 10/1/2023 - 10:7:38
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.repository.favorite.FavoriteTvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowFavViewModel @Inject constructor(
    repository: FavoriteTvRepository
): ViewModel(){
    val tvShow = repository.getAllFavoriteTvShow()
}