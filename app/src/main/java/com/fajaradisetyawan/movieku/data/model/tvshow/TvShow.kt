/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.tvshow

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "original_name")
    @SerializedName("original_name")
    var originalName: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String,

    @ColumnInfo(name = "first_air_date")
    @SerializedName("first_air_date")
    var firstAirDate: String,

    @Ignore
    @SerializedName("genre_ids")
    var genreIds: List<Int>? = arrayListOf(),

    @Ignore
    @SerializedName("origin_country")
    var originCountry: List<String>? = arrayListOf(),

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    var originalLanguage: String,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double,

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    var voteCount: Int
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}