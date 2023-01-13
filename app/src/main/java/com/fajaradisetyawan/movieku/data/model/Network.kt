/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:43
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Network(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("origin_country")
    var originCountry: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("logo_path")
    var logoPath: String? = "",
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}