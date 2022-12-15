/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:19:21
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.tv

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.data.remote.endpoint.TvShowApi
import retrofit2.HttpException
import java.io.IOException


class CrewTvShowPagingSource(
    private val tvShowApi: TvShowApi,
    private val idTv: Int
) : PagingSource<Int, Crew>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Crew> {
        return try {
            val response = tvShowApi.getTvShowCreditsPaging(idTv, BuildConfig.MOVIEDB_API_KEY)
            val crew = response.crew

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