/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import com.fajaradisetyawan.movieku.data.local.dao.favorite.FavoriteMovieDao
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FavoriteMovieRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) {
    fun addFavorite(movieDetail: MovieDetail) {
        //Exception Handling while calling insert function
        Thread {
            try {
                favoriteMovieDao.insertMovie(movieDetail)
            } catch (e: SQLiteException) {
                //handle exception
                //show message
                Log.d("LOG", "Insert Movie Error: ${e.message.toString()}")
            }
        }.start()
    }

    suspend fun getMovieById(id: Int) = favoriteMovieDao.getMovieById(id)

    suspend fun deleteFavorite(id: Int)  = favoriteMovieDao.deleteMovie(id)


    fun getFavoriteMovies():  Flow<List<MovieDetail>> {
        return favoriteMovieDao.getFavoriteMovie()
    }

    fun searchFavMovie(searchQuery: String): Flow<List<MovieDetail>> {
        return favoriteMovieDao.getSearchMovie(searchQuery)
    }
}