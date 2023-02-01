/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:48
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.data.model.CompaniesDetail
import com.fajaradisetyawan.movieku.data.model.ExternalIds
import com.fajaradisetyawan.movieku.data.remote.endpoint.MovieApi
import com.fajaradisetyawan.movieku.data.pagination.movie.*
import com.fajaradisetyawan.movieku.data.remote.response.CreditResponse
import com.fajaradisetyawan.movieku.data.remote.response.KeywordResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.CollectionResponse
import com.fajaradisetyawan.movieku.data.model.movie.MovieDetail
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieResponse
import com.fajaradisetyawan.movieku.data.remote.response.movie.MovieVideoResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: MovieApi) {

    fun getPopularMovie(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        movieApi.getPopularMovie(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailurePopularMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData
    }

    fun getTopRatedMovie(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        movieApi.getTopRatedMovie(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTopRatedMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData
    }

    fun getNowPlayingMovie(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        movieApi.getNowPlayingMovie(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureNowPlayingMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData
    }

    fun getUpcomingMovie(): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        movieApi.getUpcomingMovie(BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureUpcomingMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData
    }

    fun getDetailMovie(movieId: Int): MutableLiveData<MovieDetail?> {
        val movieDetail = MutableLiveData<MovieDetail?>()
        movieApi.getDetailMovie(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieDetail> {
                override fun onResponse(
                    call: Call<MovieDetail>,
                    response: Response<MovieDetail>
                ) {
                    movieDetail.postValue(response.body())
                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailMovie: ${t.message}")
                }

            })

        return movieDetail
    }

    fun getCreditMovie(movieId: Int): MutableLiveData<CreditResponse?> {
        val movieCredit = MutableLiveData<CreditResponse?>()
        movieApi.getMovieCredits(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<CreditResponse> {
                override fun onResponse(
                    call: Call<CreditResponse>,
                    response: Response<CreditResponse>
                ) {
                    movieCredit.postValue(response.body())
                }

                override fun onFailure(call: Call<CreditResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureCreditMovie: ${t.message}")
                }
            })
        return movieCredit
    }

    fun getRecommendationMovie(movieId: Int): MutableLiveData<MovieResponse?> {
        val liveData = MutableLiveData<MovieResponse?>()
        movieApi.getRecommendationMovie(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureRecommendationMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    liveData.postValue(response.body())
                }
            })
        return liveData
    }

    fun getKeywordMovie(movieId: Int): MutableLiveData<KeywordResponse?> {
        val liveData = MutableLiveData<KeywordResponse?>()
        movieApi.getKeywordMovie(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<KeywordResponse> {
                override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureKeywordMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<KeywordResponse>,
                    response: Response<KeywordResponse>
                ) {
                    liveData.postValue(response.body())
                }
            })
        return liveData
    }

    fun getExternalIdsMovie(movieId: Int): MutableLiveData<ExternalIds?> {
        val liveData = MutableLiveData<ExternalIds?>()
        movieApi.getExternalIdsMovie(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<ExternalIds> {
                override fun onFailure(call: Call<ExternalIds>, t: Throwable) {
                    Log.d("TAG", "onFailureExternalIdsMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<ExternalIds>,
                    response: Response<ExternalIds>
                ) {
                    liveData.postValue(response.body())
                }
            })
        return liveData
    }

    fun getTrailerMovie(movieId: Int): MutableLiveData<MovieVideoResponse?> {
        val liveData = MutableLiveData<MovieVideoResponse?>()
        movieApi.getMovieVideos(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<MovieVideoResponse> {
                override fun onFailure(call: Call<MovieVideoResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureTrailerMovie: ${t.message}")
                }

                override fun onResponse(
                    call: Call<MovieVideoResponse>,
                    response: Response<MovieVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val trailer = it.results.filter { movieVideo ->
                                movieVideo.type == "Trailer" && movieVideo.site == "YouTube"
                            }
                            if (trailer.isNotEmpty()) {
                                liveData.postValue(response.body())
                            }
                        }
                    }
                }
            })
        return liveData
    }

    fun getCollectionMovie(movieId: Int): MutableLiveData<CollectionResponse?> {
        val liveData = MutableLiveData<CollectionResponse?>()
        movieApi.getCollectionMovie(movieId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<CollectionResponse> {
                override fun onResponse(
                    call: Call<CollectionResponse>,
                    response: Response<CollectionResponse>
                ) {
                    liveData.postValue(response.body())
                }

                override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                    Log.d("TAG", "onFailureCollectionMovie: ${t.message}")
                }

            })
        return liveData
    }

    fun getMovieByKeyword(keywordId: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { MovieByKeywordPagingSource(movieApi, keywordId) }
        ).liveData


    fun getMovieByGenre(genreId: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieByGenrePagingSource(movieApi, genreId) }
        ).liveData

    fun getMovieByCompanies(companiesId: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5, //load 5 page
                maxSize = 20,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { MovieByCompaniesPagingSource(movieApi, companiesId) }
        ).liveData

    fun getDetailCompanies(companiesId: Int): MutableLiveData<CompaniesDetail?> {
        val liveData = MutableLiveData<CompaniesDetail?>()
        movieApi.getDetailCompanies(companiesId, BuildConfig.MOVIEDB_API_KEY)
            .enqueue(object : retrofit2.Callback<CompaniesDetail> {
                override fun onResponse(
                    call: Call<CompaniesDetail>,
                    response: Response<CompaniesDetail>
                ) {
                    liveData.postValue(response.body())
                }

                override fun onFailure(call: Call<CompaniesDetail>, t: Throwable) {
                    Log.d("TAG", "onFailureDetailCompanies: ${t.message}")
                }
            })
        return liveData
    }

    fun getCastMovie(idMovie: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CastMoviePagingSource(movieApi, idMovie) }
        ).liveData

    fun getCrewMovie(idMovie: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CrewMoviePagingSource(movieApi, idMovie) }
        ).liveData


}