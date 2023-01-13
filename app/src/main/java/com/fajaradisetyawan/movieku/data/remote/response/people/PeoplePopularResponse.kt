/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.response.people

import com.fajaradisetyawan.movieku.data.model.people.People
import com.google.gson.annotations.SerializedName

data class PeoplePopularResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<People>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)