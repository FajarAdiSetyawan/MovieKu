/*
 * Created by Fajar Adi Setyawan on 3/2/2023 - 10:57:39
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.watchlist

import android.database.sqlite.SQLiteException
import android.util.Log
import com.fajaradisetyawan.movieku.data.local.dao.watchlist.WatchListMovieDao
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WatchListMovieRepository @Inject constructor(
    private val watchListMovieDao: WatchListMovieDao
) {
    fun addWatchList(movieDetail: MovieDetail){
        //Exception Handling while calling insert function
        Thread {
            try {
                watchListMovieDao.insertMovie(movieDetail)
            } catch (e: SQLiteException) {
                //handle exception
                //show message
                Log.d("LOG", "Insert Movie Error: ${e.message.toString()}")
            }
        }.start()
    }

    suspend fun getMovieById(id: Int) = watchListMovieDao.getMovieById(id)

    suspend fun deleteWatchList(id: Int)  = watchListMovieDao.deleteMovie(id)

    fun getWatchListMovies(): Flow<List<MovieDetail>> {
        Log.d("TAG", "Search WL function getWL Repo")
        return watchListMovieDao.getWatchListMovie()
    }

    fun getSearchWatchList(searchQuery: String): Flow<List<MovieDetail>> {
        Log.d("TAG", "Search WL function $searchQuery Repo")
        return watchListMovieDao.getSearchWatchList(searchQuery)
    }

}