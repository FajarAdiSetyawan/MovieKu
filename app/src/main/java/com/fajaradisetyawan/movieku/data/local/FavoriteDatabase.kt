/*
 * Created by Fajar Adi Setyawan on 23/12/2022 - 12:20:41
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fajaradisetyawan.movieku.data.local.dao.FavoriteMovieDao
import com.fajaradisetyawan.movieku.data.local.dao.FavoritePeopleDao
import com.fajaradisetyawan.movieku.data.local.dao.FavoriteTvShowDao
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.data.model.people.PeopleDetail
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail

@Database(
    entities = [MovieDetail::class, TvShowDetail::class, PeopleDetail::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase(){
    abstract fun favoriteMovieDao(): FavoriteMovieDao

    abstract fun favoriteTvShowDao(): FavoriteTvShowDao

    abstract fun favoritePeopleDao(): FavoritePeopleDao
}