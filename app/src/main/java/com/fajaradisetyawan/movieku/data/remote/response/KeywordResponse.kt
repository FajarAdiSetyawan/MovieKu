/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.response

import com.fajaradisetyawan.movieku.data.model.Keyword
import com.google.gson.annotations.SerializedName

data class KeywordResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("keywords")
    val keywords: List<Keyword>,
    @SerializedName("results")
    val results: List<Keyword>,
)