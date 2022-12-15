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
data class SpokenLanguages(
    @SerializedName("iso_639_1")
    var iso: String? = "",

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("english_name")
    var englishName: String? = "",
) : Parcelable