/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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