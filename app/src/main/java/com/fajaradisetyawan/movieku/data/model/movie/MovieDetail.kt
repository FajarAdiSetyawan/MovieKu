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
import com.fajaradisetyawan.movieku.data.model.Companies
import com.fajaradisetyawan.movieku.data.model.Genre
import com.fajaradisetyawan.movieku.data.model.ProductionCountries
import com.fajaradisetyawan.movieku.data.model.SpokenLanguages
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
@Entity(tableName = "movie_detail")
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
    var overview: String,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    var releaseDate: String,

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    var originalLanguage: String,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String?,

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
    var revenue: Long,

    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    var runtime: Int,

    @ColumnInfo(name = "tagline")
    @SerializedName("tagline")
    var tagline: String,

    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: String,

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    var homepage: String,

    @Ignore
    @SerializedName("belongs_to_collection")
    var collection: Collection?,

    @Ignore
    @SerializedName("genres")
    val genre: List<Genre>,

    @Ignore
    @SerializedName("production_companies")
    val production: List<Companies>,

    @Ignore
    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountries>,

    @Ignore
    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguages>
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"

    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        0.0,
        0.0,
        0,
        0L,
        0L,
        0,
        "",
        "",
        "",
        null,
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
    )

    companion object {

        var sortByAscPopular = Comparator<MovieDetail> { o1, o2 -> o1.popularity.compareTo(o2.popularity) }

        var sortByDescPopular = Comparator<MovieDetail> { o1, o2 -> o2.popularity.compareTo(o1.popularity) }

        var sortByTitleAZ = Comparator<MovieDetail> { o1, o2 -> o1.title.compareTo(o2.title) }

        var sortByTitleZA = Comparator<MovieDetail> { o1, o2 -> o2.title.compareTo(o1.title) }

        var sortByAscVote = Comparator<MovieDetail> { o1, o2 -> o1.voteAverage.compareTo(o2.voteAverage) }

        var sortByDescVote = Comparator<MovieDetail> { o1, o2 -> o2.voteAverage.compareTo(o1.voteAverage) }
    }
}

