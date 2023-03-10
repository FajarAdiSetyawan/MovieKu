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
import com.fajaradisetyawan.movieku.data.model.*
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
@Entity(tableName = "tv_show_detail")
data class TvShowDetail(
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

    @ColumnInfo(name = "number_of_seasons")
    @SerializedName("number_of_seasons")
    var numberOfSeasons: Int,

    @ColumnInfo(name = "number_of_episodes")
    @SerializedName("number_of_episodes")
    var numberOfEpisodes: Int,

    @ColumnInfo(name = "last_air_date")
    @SerializedName("last_air_date")
    var lastAirDate: String?,

    @ColumnInfo(name = "tagline")
    @SerializedName("tagline")
    var tagline: String?,

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    var homepage: String,

    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: String,

    @ColumnInfo(name = "type")
    @SerializedName("type")
    var type: String,

    @Ignore
    @SerializedName("episode_run_time")
    var episodeRunTime: List<Int>? = arrayListOf(),

    @Ignore
    @SerializedName("languages")
    var languages: List<String>? = arrayListOf(),

    @Ignore
    @SerializedName("genres")
    val genre: List<Genre>,

    @Ignore
    @SerializedName("created_by")
    var createdBy: List<CreatedBy>,

    @Ignore
    @SerializedName("production_companies")
    val production: List<Companies>,

    @Ignore
    @SerializedName("last_episode_to_air")
    val lastEpisode: LastEpisode?,

    @Ignore
    @SerializedName("seasons")
    var seasons: List<Seasons>,

    @Ignore
    @SerializedName("networks")
    var networks: List<Network>,

    @Ignore
    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountries>,

    @Ignore
    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguages>,
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
        0,
        0,
        null,
        "",
        "",
        "",
        "",
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        null,
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
    )

    companion object {

        var sortByAscPopular = Comparator<TvShowDetail> { o1, o2 -> (o1.popularity - o2.popularity).roundToInt() }

        var sortByDescPopular = Comparator<TvShowDetail> { o1, o2 -> (o2.popularity - o1.popularity).roundToInt() }

        var sortByTitleAZ = Comparator<TvShowDetail> { o1, o2 -> o1.name.compareTo(o2.name) }

        var sortByTitleZA = Comparator<TvShowDetail> { o1, o2 -> o2.name.compareTo(o1.name) }

        var sortByAscVote = Comparator<TvShowDetail> { o1, o2 -> (o1.voteAverage - o2.voteAverage).roundToInt() }

        var sortByDescVote = Comparator<TvShowDetail> { o1, o2 -> (o2.voteAverage - o1.voteAverage).roundToInt() }
    }
}