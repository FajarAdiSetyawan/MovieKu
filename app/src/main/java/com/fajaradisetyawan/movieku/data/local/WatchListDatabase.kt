/*
 * Created by Fajar Adi Setyawan on 3/2/2023 - 10:31:34
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fajaradisetyawan.movieku.data.local.dao.watchlist.WatchListMovieDao
import com.fajaradisetyawan.movieku.data.local.dao.watchlist.WatchListTvShowDao
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail

@Database(
    entities = [MovieDetail::class, TvShowDetail::class],
    version = 1,
    exportSchema = false
)
abstract class WatchListDatabase : RoomDatabase() {
    abstract fun watchListMovieDao(): WatchListMovieDao
    abstract fun watchListTvDao(): WatchListTvShowDao
}