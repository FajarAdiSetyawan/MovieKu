/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
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