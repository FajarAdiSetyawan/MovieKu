/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail

@Dao
interface FavoriteTvShowDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insert(tvShow: TvShowDetail)

    @Query("DELETE FROM favorite_tv_show WHERE favorite_tv_show.id = :id" )
    suspend fun deleteTvShow(id: Int) : Int

    @Query("SELECT * FROM favorite_tv_show ORDER BY popularity ASC")
    fun getFavoriteTvShow(): LiveData<List<TvShowDetail>>

    @Query("SELECT count(*) FROM favorite_tv_show where favorite_tv_show.id =:id")
    suspend fun getTvShowById(id: Int): Int


}