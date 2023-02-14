/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.people

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fajaradisetyawan.movieku.data.model.tvshow.TvShowDetail
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
@Entity(tableName = "favorite_people")
data class PeopleDetail(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    var gender: Int = 0,

    @ColumnInfo(name = "place_of_birth")
    @SerializedName("place_of_birth")
    var place: String?,

    @ColumnInfo(name = "homepage")
    @SerializedName("homepage")
    var homepage: String?,

    @ColumnInfo(name = "known_for_department")
    @SerializedName("known_for_department")
    var department: String,

    @ColumnInfo(name = "profile_path")
    @SerializedName("profile_path")
    var profilePath: String?,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Double = 0.0,

    @ColumnInfo(name = "birthday")
    @SerializedName("birthday")
    var birthday: String?,

    @ColumnInfo(name = "deathday")
    @SerializedName("deathday")
    var deathday: String?,

    @ColumnInfo(name = "biography")
    @SerializedName("biography")
    var biography: String,

    @Ignore
    @SerializedName("also_known_as")
    var alsoKnownAs: List<String>,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
    constructor() : this(
        0,
        "",
        0,
        null,
        null,
        "",
        null,
        0.0,
        null,
        null,
        "",
        arrayListOf()
    )

    companion object {

        var sortByAscPopular = Comparator<PeopleDetail> { o1, o2 -> (o1.popularity - o2.popularity).roundToInt() }

        var sortByDescPopular = Comparator<PeopleDetail> { o1, o2 -> (o2.popularity - o1.popularity).roundToInt() }

        var sortByTitleAZ = Comparator<PeopleDetail> { o1, o2 -> o1.name.compareTo(o2.name) }

        var sortByTitleZA = Comparator<PeopleDetail> { o1, o2 -> o2.name.compareTo(o1.name) }
    }
}