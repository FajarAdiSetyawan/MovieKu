/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieCast(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("original_title")
    var originalTitle: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("release_date")
    var releaseDate: String,

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = arrayListOf(),

    @SerializedName("original_language")
    var originalLanguage: String,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("character")
    var character: String,

    @SerializedName("credit_id")
    var creditId: String,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("vote_count")
    var voteCount: Int
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}
