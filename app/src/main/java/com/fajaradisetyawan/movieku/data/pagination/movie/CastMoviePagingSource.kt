/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.pagination.movie

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.remote.endpoint.MovieApi
import retrofit2.HttpException
import java.io.IOException


class CastMoviePagingSource(
    private val movieApi: MovieApi,
    private val idMovie: Int
) : PagingSource<Int, Cast>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cast> {
        return try {
            val response = movieApi.getMovieCreditsPaging(idMovie, BuildConfig.MOVIEDB_API_KEY)
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