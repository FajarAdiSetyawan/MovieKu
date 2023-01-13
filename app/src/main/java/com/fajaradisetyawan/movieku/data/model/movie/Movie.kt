/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.movie

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    var originalTitle: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: String,

    @Ignore
    @SerializedName("genre_ids")
    var genreIds: List<Int>? = arrayListOf(),

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
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}