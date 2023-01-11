/*
 * Created by Fajar Adi Setyawan on 10/1/2023 - 10:8:29
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository.favorite

import com.fajaradisetyawan.movieku.data.local.dao.FavoriteTvShowDao
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import javax.inject.Inject


class FavoriteTvRepository @Inject constructor(
    private val tvShowDao: FavoriteTvShowDao
) {

    suspend fun addFavorite(tvShowDetail: TvShowDetail) = tvShowDao.insert(tvShowDetail)

    suspend fun deleteFavorite(id: Int) = tvShowDao.deleteTvShow(id)

    suspend fun getTvShowId(id: Int) = tvShowDao.getTvShowById(id)

    fun getAllFavoriteTvShow() = tvShowDao.getFavoriteTvShow()
}