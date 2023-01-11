/*
 * Created by Fajar Adi Setyawan on 10/1/2023 - 10:9:20
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.feature.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.movieku.repository.favorite.FavoritePeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleFavViewModel @Inject constructor(
    favoritePeopleRepository: FavoritePeopleRepository
): ViewModel() {

    val people = favoritePeopleRepository.getFavoritePeople()
}