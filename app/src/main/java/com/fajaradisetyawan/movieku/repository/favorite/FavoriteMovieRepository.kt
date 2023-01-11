/*
 * Created by Fajar Adi Setyawan on 23/12/2022 - 12:26:46
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.fajaradisetyawan.movieku.data.local.dao.FavoriteMovieDao
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import javax.inject.Inject


class FavoriteMovieRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) {
    suspend fun addFavorite(movieDetail: MovieDetail) = favoriteMovieDao.insertMovie(movieDetail)

    suspend fun getMovieById(id: Int) = favoriteMovieDao.getMovieById(id)

    suspend fun deleteFavorite(id: Int)  = favoriteMovieDao.deleteMovie(id)

    fun getFavoriteMovies() = favoriteMovieDao.getFavoriteMovie()
}