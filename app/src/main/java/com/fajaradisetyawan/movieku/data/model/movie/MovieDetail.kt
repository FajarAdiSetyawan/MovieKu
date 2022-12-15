/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.movie

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fajaradisetyawan.movieku.data.model.Companies
import com.fajaradisetyawan.movieku.data.model.Genre
import com.fajaradisetyawan.movieku.data.model.ProductionCountries
import com.fajaradisetyawan.movieku.data.model.SpokenLanguages
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetail(
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
    var overview: String? = "",

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: String,

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
    var voteCount: Int,

    @ColumnInfo(name = "budget")
    @SerializedName("budget")
    var budget: Long,

    @ColumnInfo(name = "revenue")
    @SerializedName("revenue")
    var revenue: Long = 0,

    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    var runtime: Int = 0,

    @ColumnInfo(name = "tagline")
    @SerializedName("tagline")
    var tagline: String? = null,

    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: String? = "",

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    var homepage: String? = "",

    @Ignore
    @SerializedName("belongs_to_collection")
    var collection: Collection? = null,

    @Ignore
    @SerializedName("genres")
    var genreIds: MutableList<Genre> = ArrayList(),

    @Ignore
    @SerializedName("production_companies")
    var productionCompanies: MutableList<Companies> = ArrayList(),

    @Ignore
    @SerializedName("production_countries")
    var productionCountries: MutableList<ProductionCountries> = ArrayList(),

    @Ignore
    @SerializedName("spoken_languages")
    var spokenLanguages: MutableList<SpokenLanguages> = ArrayList()

) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}