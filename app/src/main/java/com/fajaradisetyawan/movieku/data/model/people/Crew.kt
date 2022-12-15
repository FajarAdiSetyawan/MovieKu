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
data class Crew(
    @SerializedName("id")
    val id: Int,
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("known_for_department")
    val knowDepartment: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("job")
    val job: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("profile_path")
    val profilePath: String? = null,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("jobs")
    val jobs: List<Job>,
    @SerializedName("total_episode_count")
    val totalEpisodeCount: Int,
): Parcelable {
    val baseUrl get() = "https://image.tmdb.org/t/p/w500"
}