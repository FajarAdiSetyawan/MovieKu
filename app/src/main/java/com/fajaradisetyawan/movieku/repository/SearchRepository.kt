/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.fajaradisetyawan.movieku.data.pagination.search.*
import com.fajaradisetyawan.movieku.data.remote.endpoint.SearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val searchApi: SearchApi) {
    fun getSearchMovie(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { MovieSearchPagingSource(searchApi, query) }
        ).liveData

    fun getSearchTvShow(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { TvShowSearchPagingSource(searchApi, query) }
        ).liveData

    fun getSearchKeyword(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { KeywordSearchPagingSource(searchApi, query) }
        ).liveData

    fun getSearchCollection(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { CollectionSearchPagingSource(searchApi, query) }
        ).liveData

    fun getSearchCompany(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { CompanySearchPagingSource(searchApi, query) }
        ).liveData


    fun getSearchPeople(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { PeopleSearchPagingSource(searchApi, query) }
        ).liveData
}