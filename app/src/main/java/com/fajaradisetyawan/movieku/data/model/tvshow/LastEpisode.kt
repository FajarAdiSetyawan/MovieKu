/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastEpisode(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("episode_number")
    var episodeNumber: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("air_date")
    var airDate: String,

    @SerializedName("production_code")
    var productionCode: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("still_path")
    var stillPath: String? = "",

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int,

    @SerializedName("season_number")
    var seasonNumber: Int,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}