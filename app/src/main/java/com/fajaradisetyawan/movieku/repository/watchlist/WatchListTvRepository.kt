/*
 * Created by Fajar Adi Setyawan on 3/2/2023 - 10:57:47
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.watchlist

import android.database.sqlite.SQLiteException
import android.util.Log
import com.fajaradisetyawan.movieku.data.local.dao.watchlist.WatchListTvShowDao
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WatchListTvRepository @Inject constructor(
    private val watchListTvShowDao: WatchListTvShowDao
) {
    fun addWatchList(tvShowDetail: TvShowDetail) {
        //Exception Handling while calling insert function
        Thread {
            try {
                watchListTvShowDao.insert(tvShowDetail)
            } catch (e: SQLiteException) {
                //handle exception
                //show message
                Log.d("LOG", "Insert Tv Error: ${e.message.toString()}")
            }
        }.start()
    }

    suspend fun deleteWatchList(id: Int) = watchListTvShowDao.deleteTvShow(id)

    suspend fun getTvShowId(id: Int) = watchListTvShowDao.getTvShowById(id)

    fun getWatchListTvShow(): Flow<List<TvShowDetail>> {
        return watchListTvShowDao.getWatchListTvShow()
    }

    fun searchTvShow(searchQuery: String): Flow<List<TvShowDetail>> {
        return watchListTvShowDao.getSearchTv(searchQuery)
    }
}