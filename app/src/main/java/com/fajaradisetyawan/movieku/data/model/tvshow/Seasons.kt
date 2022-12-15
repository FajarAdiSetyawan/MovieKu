/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Seasons(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("season_number")
    var seasonNumber: Int = 0,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("episode_count")
    var episodeCount: Int = 0,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("air_date")
    var airDate: String? = null,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("episodes")
    var episodes: MutableList<Episode> = ArrayList(),
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}