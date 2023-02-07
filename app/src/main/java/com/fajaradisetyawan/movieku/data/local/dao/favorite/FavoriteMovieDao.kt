/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao.favorite

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insertMovie(movieDetail: MovieDetail)

    @Query("DELETE FROM movie_detail WHERE movie_detail.id = :id" )
    suspend fun deleteMovie(id: Int) : Int

    @Query("SELECT * FROM movie_detail ORDER BY popularity ASC")
    fun getFavoriteMovie(): Flow<List<MovieDetail>>

    @Query("SELECT count(*) FROM movie_detail WHERE movie_detail.id = :id")
    suspend fun getMovieById(id: Int): Int

    @Query("SELECT * FROM movie_detail WHERE movie_detail.title LIKE :search OR movie_detail.original_title LIKE :search")
    fun getSearchMovie(search: String?): Flow<List<MovieDetail>>
}