/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.people

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShowCrewPeople(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("original_name")
    var originalName: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("first_air_date")
    var firstAirDate: String,

    @SerializedName("origin_country")
    var originCountry: List<String>? = arrayListOf(),

    @SerializedName("original_language")
    var originalLanguage: String,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("job")
    var job: String,

    @SerializedName("department")
    var department: String,

    @SerializedName("credit_id")
    var creditId: String,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int,

    @SerializedName("episode_count")
    var episodeCount: Int,
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}