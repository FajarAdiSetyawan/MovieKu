/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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