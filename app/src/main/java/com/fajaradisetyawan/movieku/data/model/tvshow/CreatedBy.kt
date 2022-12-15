/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatedBy(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("credit_id")
    var creditId: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("gender")
    var gender: Int,

    @SerializedName("profile_path")
    var profilePath: String? = null,
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}