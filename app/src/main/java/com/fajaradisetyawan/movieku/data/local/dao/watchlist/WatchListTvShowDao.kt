/*
 * Created by Fajar Adi Setyawan on 3/2/2023 - 10:30:51
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao.watchlist

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListTvShowDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insert(tvShow: TvShowDetail)

    @Query("DELETE FROM tv_show_detail WHERE tv_show_detail.id = :id")
    suspend fun deleteTvShow(id: Int): Int

    @Query("SELECT * FROM tv_show_detail ORDER BY popularity ASC")
    fun getWatchListTvShow(): Flow<List<TvShowDetail>>

    @Query("SELECT count(*) FROM tv_show_detail where tv_show_detail.id =:id")
    suspend fun getTvShowById(id: Int): Int

    @Query("SELECT * FROM tv_show_detail WHERE tv_show_detail.name LIKE :search OR tv_show_detail.original_name LIKE :search")
    fun getSearchTv(search: String?): Flow<List<TvShowDetail>>
}