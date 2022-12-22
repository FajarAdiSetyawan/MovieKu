/*
 * Created by Fajar Adi Setyawan on 15/12/2022 - 10:16:3
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.movie

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.data.remote.endpoint.MovieApi
import retrofit2.HttpException
import java.io.IOException


class CrewMoviePagingSource(
    private val movieApi: MovieApi,
    private val idMovie: Int
) : PagingSource<Int, Crew>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Crew> {
        return try {
            val response = movieApi.getMovieCreditsPaging(idMovie, BuildConfig.MOVIEDB_API_KEY)
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