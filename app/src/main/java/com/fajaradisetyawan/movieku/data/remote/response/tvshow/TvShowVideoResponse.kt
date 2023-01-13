/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.response.tvshow

import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowVideo
import com.google.gson.annotations.SerializedName

data class TvShowVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<TvShowVideo>
)