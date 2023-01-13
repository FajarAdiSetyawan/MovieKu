/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:44
 * fajaras465@gmail.com
 * Copyright (c) 2023.
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