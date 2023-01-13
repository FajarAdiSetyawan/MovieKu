/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.pagination.search

import androidx.paging.PagingSource
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.Companies
import com.fajaradisetyawan.movieku.data.remote.endpoint.SearchApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class CompanySearchPagingSource(
    private val searchApi: SearchApi,
    private val query: String
) : PagingSource<Int, Companies>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Companies> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = searchApi.searchCompany(BuildConfig.MOVIEDB_API_KEY, query, position)
            val result = response.results

            LoadResult.Page(
                data = result,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (result.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}