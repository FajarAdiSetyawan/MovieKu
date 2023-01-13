/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.data.remote.response.tvshow

import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.fajaradisetyawan.movieku.data.model.tvshow.GuestStar
import com.google.gson.annotations.SerializedName

data class TvEpisodeCreditResponse(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("crew")
    val crew: List<Crew>,

    @SerializedName("cast")
    val cast: List<Cast>,

    @SerializedName("guest_stars")
    val guestStar: List<GuestStar>,
)