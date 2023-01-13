/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail

@Dao
interface FavoritePeopleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(peopleDetail: PeopleDetail)

    @Query("DELETE FROM favorite_people WHERE favorite_people.id = :id" )
    suspend fun deletePeople(id: Int) : Int

    @Query("SELECT * FROM favorite_people ORDER BY popularity ASC")
    fun getFavoritePeople(): LiveData<List<PeopleDetail>>

    @Query("SELECT count(*) FROM favorite_people where favorite_people.id = :id")
    suspend fun getPeopleById(id: Int): Int
}