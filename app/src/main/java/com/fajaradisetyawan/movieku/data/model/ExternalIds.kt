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
data class ExternalIds(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("imdb_id")
    var imdbId: String? = "",

    @SerializedName("facebook_id")
    var facebookId: String? = "",

    @SerializedName("instagram_id")
    var instagramId: String? = "",

    @SerializedName("twitter_id")
    var twitterId: String? = "",

    @SerializedName("freebase_mid")
    var freebaseMid: String? = "",

    @SerializedName("freebase_id")
    var freebaseId: String? = "",

    @SerializedName("tvrage_id")
    var tvRageId: String? = "",
): Parcelable