/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.tvshow

import com.fajaradisetyawan.movieku.data.model.tvshow.Episode
import com.google.gson.annotations.SerializedName

data class TvSeasonResponse(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("season_number")
    var seasonNumber: Int = 0,

    @SerializedName("name")
    var name: String,

    @SerializedName("episode_count")
    var episodeCount: Int,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("air_date")
    var airDate: String? = null,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("episodes")
    val episodes: List<Episode>,
)