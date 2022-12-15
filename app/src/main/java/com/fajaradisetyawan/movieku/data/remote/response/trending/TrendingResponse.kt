/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.trending

import com.fajaradisetyawan.movieku.data.model.Trending
import com.google.gson.annotations.SerializedName

data class TrendingResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Trending>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
