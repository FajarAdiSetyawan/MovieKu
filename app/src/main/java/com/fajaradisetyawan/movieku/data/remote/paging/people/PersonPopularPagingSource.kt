/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.paging.people

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.people.People
import com.fajaradisetyawan.movieku.data.remote.endpoint.PeopleApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX_PAGE = 1

class PersonPopularPagingSource(
    private val peopleApi: PeopleApi
) : PagingSource<Int, People>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, People> {
        return try {
            val position = params.key ?: STARTING_INDEX_PAGE
            val response = peopleApi.getPeoplePopular(BuildConfig.MOVIEDB_API_KEY, position)
            val person = response.results

            LoadResult.Page(
                data = person,
                prevKey = if (position == STARTING_INDEX_PAGE) null else position -1,
                nextKey = if (person.isEmpty()) null else position + 1
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}