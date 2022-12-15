/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 10:53:30
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException

class SeasonPagingSource(
    private val tvShowApi: TvShowApi,
    private val idTvShow: Int
) : PagingSource<Int, Seasons>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Seasons> {
        return try {
            val response = tvShowApi.getAllSeasonTvShow(idTvShow, BuildConfig.MOVIEDB_API_KEY)
            val seasons = response.seasons

            LoadResult.Page(
                data = seasons,
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