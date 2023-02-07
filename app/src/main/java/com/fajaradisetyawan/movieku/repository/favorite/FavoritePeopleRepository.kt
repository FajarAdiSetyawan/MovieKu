/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import com.fajaradisetyawan.movieku.data.local.dao.favorite.FavoritePeopleDao
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FavoritePeopleRepository @Inject constructor(
    private val favoritePeopleDao: FavoritePeopleDao
) {
    fun addFavorite(peopleDetail: PeopleDetail) {
        //Exception Handling while calling insert function
        Thread {
            try {
                favoritePeopleDao.insert(peopleDetail)
            } catch (e: SQLiteException) {
                //handle exception
                //show message
                Log.d("LOG", "Insert People Error: ${e.message.toString()}")
            }
        }.start()
    }

    suspend fun deleteFavorite(id: Int) = favoritePeopleDao.deletePeople(id)

    suspend fun getPeopleById(id: Int) = favoritePeopleDao.getPeopleById(id)

    fun getFavoritePeople(): Flow<List<PeopleDetail>> {
        return favoritePeopleDao.getFavoritePeople()
    }

    fun searchFavPeople(searchQuery: String): Flow<List<PeopleDetail>> {
        return favoritePeopleDao.getSearchPeople(searchQuery)
    }
}