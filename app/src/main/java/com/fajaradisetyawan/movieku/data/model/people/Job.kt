/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.model.people

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Job(
    @SerializedName("credit_id")
    var id: String,

    @SerializedName("job")
    var job: String,

    @SerializedName("episode_count")
    var episodeCount: Int = 0,
): Parcelable