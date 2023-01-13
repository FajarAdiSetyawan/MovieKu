/*
 * Created by Fajar Adi Setyawan on 13/1/2023 - 10:17:42
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.di

import android.content.Context
import androidx.room.Room
import com.fajaradisetyawan.movieku.data.local.FavoriteDatabase
import com.fajaradisetyawan.movieku.data.remote.endpoint.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTrendingApi(retrofit: Retrofit): TrendingApi =
        retrofit.create(TrendingApi::class.java)

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideTvShowApi(retrofit: Retrofit): TvShowApi =
        retrofit.create(TvShowApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun providePersonApi(retrofit: Retrofit): PeopleApi =
        retrofit.create(PeopleApi::class.java)


    @Singleton
    @Provides
    fun provideFavDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FavoriteDatabase::class.java,
        "movie_db"
    ).build()

    @Singleton
    @Provides
    fun provideFavMovieDao(db: FavoriteDatabase) = db.favoriteMovieDao()

    @Singleton
    @Provides
    fun provideFavTvDao(db: FavoriteDatabase) = db.favoriteTvShowDao()

    @Singleton
    @Provides
    fun provideFavPeopleDao(db: FavoriteDatabase) = db.favoritePeopleDao()


}