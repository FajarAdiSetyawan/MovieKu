/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trending(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("original_title")
    val originalTitle: String? = "", // checking null data

    @SerializedName("original_name")
    val originalName: String? = "", // checking null data

    @SerializedName("title")
    var title: String? = "",

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("media_type")
    var mediaType: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("first_air_date")
    val firstAirDate: String? = "", // checking null data

    @SerializedName("release_date")
    val releaseDate: String? = "", // checking null data

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = arrayListOf(),

    @SerializedName("original_language")
    var originalLanguage: String,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}