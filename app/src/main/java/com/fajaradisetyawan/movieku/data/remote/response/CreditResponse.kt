/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response

import com.fajaradisetyawan.movieku.data.model.people.Cast
import com.fajaradisetyawan.movieku.data.model.people.Crew
import com.google.gson.annotations.SerializedName

data class CreditResponse(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)