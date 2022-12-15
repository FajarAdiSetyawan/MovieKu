/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.people

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeopleDetail(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String,

    @SerializedName("gender")
    var gender: Int = 0,

    @SerializedName("place_of_birth")
    var place: String? = null,

    @SerializedName("homepage")
    var homepage: String? = null,

    @SerializedName("known_for_department")
    var department: String? = "",

    @SerializedName("profile_path")
    var profilePath: String? = null,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("birthday")
    var birthday: String? = null,

    @SerializedName("deathday")
    var deathday: String? = null,

    @SerializedName("biography")
    var biography: String? = "",

    @SerializedName("also_known_as")
    var alsoKnownAs: List<String>,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}