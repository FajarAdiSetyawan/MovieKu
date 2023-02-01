/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local.dao

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    fun insertMovie(movieDetail: MovieDetail)

    @Query("DELETE FROM favorite_movie WHERE favorite_movie.id = :id" )
    suspend fun deleteMovie(id: Int) : Int

    @Query("SELECT * FROM favorite_movie ORDER BY popularity ASC")
    fun getFavoriteMovie(): LiveData<List<MovieDetail>>

    @Query("SELECT count(*) FROM favorite_movie WHERE favorite_movie.id = :id")
    suspend fun getMovieById(id: Int): Int
}