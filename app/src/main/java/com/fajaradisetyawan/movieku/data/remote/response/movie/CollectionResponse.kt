/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.movie

import android.os.Parcelable
import com.fajaradisetyawan.movieku.data.model.movie.Movie
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionResponse(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("parts")
    val parts: List<Movie>,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}