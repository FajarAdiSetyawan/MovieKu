/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:49:46
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException


class CastEpisodePagingSource(
    private val tvShowApi: TvShowApi,
    private val idTv: Int,
    private val idSeason: Int,
    private val idEpisode: Int
) : PagingSource<Int, Cast>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cast> {
        return try {
            val response = tvShowApi.getCreditEpisodeTvShow(idTv, idSeason, idEpisode, BuildConfig.MOVIEDB_API_KEY)
            val cast = response.cast

            LoadResult.Page(
                data = cast,
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