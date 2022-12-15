/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.tvshow

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.fajaradisetyawan.movieku.data.model.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShowDetail(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "original_name")
    @SerializedName("original_name")
    var originalName: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("overview")
    var overview: String? = null,

    @ColumnInfo(name = "first_air_date")
    @SerializedName("first_air_date")
    var firstAirDate: String,

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    var originalLanguage: String,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("popularity")
    var popularity: Double,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double,

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    var voteCount: Int,

    @ColumnInfo(name = "budget")
    @SerializedName("number_of_seasons")
    var numberOfSeasons: Int,

    @SerializedName("number_of_episodes")
    var numberOfEpisodes: Int = 0,

    @SerializedName("last_air_date")
    var lastAirDate: String? = null,

    @SerializedName("tagline")
    var tagline: String? = "",

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    var homepage: String? = "",

    @SerializedName("status")
    var status: String? = "",

    @SerializedName("type")
    var type: String? = "",

    @SerializedName("episode_run_time")
    var episodeRunTime: List<Int>? = arrayListOf(),

    @SerializedName("languages")
    var languages: List<String>? = arrayListOf(),

    @SerializedName("genres")
    var genreIds: MutableList<Genre> = ArrayList(),

    @SerializedName("created_by")
    var createdBy: MutableList<CreatedBy> = ArrayList(),

    @SerializedName("production_companies")
    var productionCompanies: MutableList<Companies> = ArrayList(),

    @SerializedName("last_episode_to_air")
    val lastEpisode: MutableList<LastEpisode> = ArrayList(),

    @SerializedName("seasons")
    var seasons: MutableList<Seasons> = ArrayList(),

    @SerializedName("networks")
    var networks: MutableList<Network> = ArrayList(),

    @SerializedName("production_countries")
    var productionCountries: MutableList<ProductionCountries> = ArrayList(),

    @SerializedName("spoken_languages")
    var spokenLanguages: MutableList<SpokenLanguages> = ArrayList(),
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}