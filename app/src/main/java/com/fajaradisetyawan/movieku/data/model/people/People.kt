/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.people

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class People(
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    var gender: Int = 0,

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    var adult: Boolean,

    @Ignore
    @SerializedName("known_for")
    var knownFor: List<KnownFor>?,

    @ColumnInfo(name = "known_for_department")
    @SerializedName("known_for_department")
    var knownForDepartment: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String? = null,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    var popularity: Double,

    @ColumnInfo(name = "profile_path")
    @SerializedName("profile_path")
    var profilePath: String? = null
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}