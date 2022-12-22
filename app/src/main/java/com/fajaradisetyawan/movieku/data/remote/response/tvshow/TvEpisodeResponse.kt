/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.tvshow

import android.os.Parcelable
import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.data.model.tvshow.GuestStar
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvEpisodeResponse(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("season_number")
    var seasonNumber: Int = 0,

    @SerializedName("episode_number")
    var episodeNumber: Int = 0,

    @SerializedName("show_id")
    var idTvShow: Int = 0,

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("production_code")
    var productionCode: String? = "",

    @SerializedName("still_path")
    var stillPath: String? = null,

    @SerializedName("air_date")
    var airDate: String? = null,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int,

    @SerializedName("runtime")
    var runtime: Int,

    @SerializedName("crew")
    val crew: List<Crew>,

    @SerializedName("guest_stars")
    val guestStar: List<GuestStar>,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w400"
}