/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompaniesDetail(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("headquarters")
    var headquarters: String? = null,

    @SerializedName("homepage")
    var homepage: String? = null,

    @SerializedName("origin_country")
    var originCountry: String? = null,

    @SerializedName("logo_path")
    var logoPath: String? = null,
) : Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}