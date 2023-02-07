/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao.favorite

import android.database.sqlite.SQLiteException
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePeopleDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insert(peopleDetail: PeopleDetail)

    @Query("DELETE FROM favorite_people WHERE favorite_people.id = :id" )
    suspend fun deletePeople(id: Int) : Int

    @Query("SELECT * FROM favorite_people ORDER BY popularity ASC")
    fun getFavoritePeople(): Flow<List<PeopleDetail>>

    @Query("SELECT count(*) FROM favorite_people where favorite_people.id = :id")
    suspend fun getPeopleById(id: Int): Int

    @Query("SELECT * FROM favorite_people WHERE favorite_people.name LIKE :search")
    fun getSearchPeople(search: String?): Flow<List<PeopleDetail>>
}