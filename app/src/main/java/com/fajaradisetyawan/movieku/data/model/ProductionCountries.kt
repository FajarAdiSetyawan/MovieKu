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
data class ProductionCountries(
    @SerializedName("iso_3166_1")
    var iso: String? = "",

    @SerializedName("name")
    var name: String? = "",
): Parcelable