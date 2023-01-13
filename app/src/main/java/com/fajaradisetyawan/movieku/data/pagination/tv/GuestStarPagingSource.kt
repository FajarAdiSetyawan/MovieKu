/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.pagination.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.tvshow.GuestStar
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException


class GuestStarPagingSource(
    private val tvShowApi: TvShowApi,
    private val idTv: Int,
    private val idSeason: Int,
    private val idEpisode: Int
) : PagingSource<Int, GuestStar>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GuestStar> {
        return try {
            val response = tvShowApi.getCreditEpisodeTvShow(idTv, idSeason, idEpisode, BuildConfig.MOVIEDB_API_KEY)
            val guestStar = response.guestStar

            LoadResult.Page(
                data = guestStar,
                prevKey = null,
                nextKey = null
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}