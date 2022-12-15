/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.people

import com.fajaradisetyawan.movieku.data.model.people.TvShowCastPeople
import com.fajaradisetyawan.movieku.data.model.people.TvShowCrewPeople
import com.google.gson.annotations.SerializedName

data class TvCreditPeopleResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val castTvShow: List<TvShowCastPeople>,
    @SerializedName("crew")
    val crewTvShow: List<TvShowCrewPeople>,
)