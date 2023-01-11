/*
 * Created by Fajar Adi Setyawan on 10/1/2023 - 10:8:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import com.fajaradisetyawan.movieku.data.local.dao.FavoritePeopleDao
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import javax.inject.Inject


class FavoritePeopleRepository @Inject constructor(
    private val favoritePeopleDao: FavoritePeopleDao
) {
    suspend fun addFavorite(peopleDetail: PeopleDetail) = favoritePeopleDao.insert(peopleDetail)

    suspend fun deleteFavorite(id: Int) = favoritePeopleDao.deletePeople(id)

    suspend fun getPeopleById(id: Int) = favoritePeopleDao.getPeopleById(id)

    fun getFavoritePeople() = favoritePeopleDao.getFavoritePeople()
}