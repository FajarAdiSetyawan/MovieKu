/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.pagination.movie

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.movie.Movie
import com.fajaradisetyawan.movieku.data.remote.endpoint.MovieApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX_PAGE = 1

class MovieByCompaniesPagingSource(
    private val movieApi: MovieApi,
    private val companiesId: String
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: STARTING_INDEX_PAGE
            val response = movieApi.getMovieByCompanies(BuildConfig.MOVIEDB_API_KEY, companiesId, position)
            val movie = response.results

            LoadResult.Page(
                data = movie,
                prevKey = if (position == STARTING_INDEX_PAGE) null else position -1,
                nextKey = if (movie.isEmpty()) null else position + 1
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}