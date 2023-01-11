/*
 * Created by Fajar Adi Setyawan on 23/12/2022 - 12:1:34
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail

@Dao
interface FavoriteTvShowDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tvShow: TvShowDetail)

    @Query("DELETE FROM favorite_tv_show WHERE favorite_tv_show.id = :id" )
    suspend fun deleteTvShow(id: Int) : Int

    @Query("SELECT * FROM favorite_tv_show ORDER BY popularity ASC")
    fun getFavoriteTvShow(): LiveData<List<TvShowDetail>>

    @Query("SELECT count(*) FROM favorite_tv_show where favorite_tv_show.id =:id")
    suspend fun getTvShowById(id: Int): Int

}