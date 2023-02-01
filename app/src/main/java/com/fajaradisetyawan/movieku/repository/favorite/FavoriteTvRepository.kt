/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import android.database.sqlite.SQLiteException
import android.util.Log
import com.fajaradisetyawan.movieku.data.local.dao.FavoriteTvShowDao
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.fajaradisetyawan.movieku.utils.CustomToastDialog
import javax.inject.Inject


class FavoriteTvRepository @Inject constructor(
    private val tvShowDao: FavoriteTvShowDao
) {

    fun addFavorite(tvShowDetail: TvShowDetail) {
        //Exception Handling while calling insert function
        Thread {
            try {
                tvShowDao.insert(tvShowDetail)
            } catch (e: SQLiteException) {
                //handle exception
                //show message
                Log.d("LOG", "Insert Tv Error: ${e.message.toString()}")
            }
        }.start()
    }

    suspend fun deleteFavorite(id: Int) = tvShowDao.deleteTvShow(id)

    suspend fun getTvShowId(id: Int) = tvShowDao.getTvShowById(id)

    fun getAllFavoriteTvShow() = tvShowDao.getFavoriteTvShow()


}