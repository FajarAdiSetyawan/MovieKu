/*
 * Created by Fajar Adi Setyawan on 12/12/2022 - 10:53:42
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.fajaradisetyawan.movieku.data.model.tvshow.Seasons
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException


class EpisodePagingSource(
    private val tvShowApi: TvShowApi,
    private val idTvShow: Int,
    private val idSeasons: Int,
) : PagingSource<Int, Episode>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        return try {
            val response = tvShowApi.getAllEpisodeTvShow(idTvShow, idSeasons, BuildConfig.MOVIEDB_API_KEY)
            val episode = response.episodes

            LoadResult.Page(
                data = episode,
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