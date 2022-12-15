/*
 * Created by Fajar Adi Setyawan on 8/12/2022 - 11:52:40
 * fajaras465@gmail.com
 * Copyright (c) 2022.
 */

package com.fajaradisetyawan.movieku.data.remote.response.people

import com.fajaradisetyawan.movieku.data.model.people.MovieCastPeople
import com.fajaradisetyawan.movieku.data.model.people.MovieCrewPeople
import com.google.gson.annotations.SerializedName

data class MovieCreditPeopleResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val castMovie: List<MovieCastPeople>,
    @SerializedName("crew")
    val crewMovie: List<MovieCrewPeople>,
)