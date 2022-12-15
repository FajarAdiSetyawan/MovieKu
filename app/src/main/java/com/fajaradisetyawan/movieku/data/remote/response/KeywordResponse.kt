/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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