/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.response.search

import com.fajaradisetyawan.movieku.data.model.Keyword
import com.google.gson.annotations.SerializedName


data class SearchKeywordResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Keyword>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)