/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShow
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX_PAGE = 1

class TvByNetworkPagingSource(
    private val tvShowApi: TvShowApi,
    private val networkId: String
) : PagingSource<Int, TvShow>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        return try {
            val position = params.key ?: STARTING_INDEX_PAGE
            val response = tvShowApi.getTvShowByNetwork(BuildConfig.MOVIEDB_API_KEY, networkId, position)
            val tvShow = response.results

            LoadResult.Page(
                data = tvShow,
                prevKey = if (position == STARTING_INDEX_PAGE) null else position -1,
                nextKey = if (tvShow.isEmpty()) null else position + 1
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}