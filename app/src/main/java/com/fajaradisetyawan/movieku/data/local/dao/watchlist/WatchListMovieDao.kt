/*
 * Created by Fajar Adi Setyawan on 3/2/2023 - 10:30:32
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
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListMovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insertMovie(movieDetail: MovieDetail)

    @Query("DELETE FROM movie_detail WHERE movie_detail.id = :id" )
    suspend fun deleteMovie(id: Int) : Int

    @Query("SELECT * FROM movie_detail ORDER BY popularity ASC")
    fun getWatchListMovie(): Flow<List<MovieDetail>>

    @Query("SELECT count(*) FROM movie_detail WHERE movie_detail.id = :id")
    suspend fun getMovieById(id: Int): Int

    @Query("SELECT * FROM movie_detail WHERE movie_detail.title LIKE :search OR movie_detail.original_title LIKE :search")
    fun getSearchMovie(search: String?): Flow<List<MovieDetail>>
}