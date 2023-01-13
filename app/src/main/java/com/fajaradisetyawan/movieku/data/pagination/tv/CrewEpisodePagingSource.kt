/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.pagination.tv

import android.util.Log
import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException


class CrewEpisodePagingSource(
    private val tvShowApi: TvShowApi,
    private val idTv: Int,
    private val idSeason: Int,
    private val idEpisode: Int
) : PagingSource<Int, Crew>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Crew> {
        return try {
            val response = tvShowApi.getCreditEpisodeTvShow(idTv, idSeason, idEpisode, BuildConfig.MOVIEDB_API_KEY)
            val crew = response.crew
            Log.d("TAG", "onSuccessCrewEpisode: idTv: $idTv idSeason: $idSeason, idEpisode: $idEpisode")
            LoadResult.Page(
                data = crew,
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