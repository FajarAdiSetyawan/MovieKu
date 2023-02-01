/*
 * Created by Fajar Adi Setyawan on 23/1/2023 - 11:12:9
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.fajaradisetyawan.movieku.BuildConfig
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.data.model.movie.Movie
import com.fajaradisetyawan.movieku.data.remote.endpoint.MovieApi
import com.fajaradisetyawan.movieku.feature.ui.MainActivity
import com.fajaradisetyawan.movieku.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class NewReleaseMovieWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val movieApi: MovieApi
) :
    Worker(context, workerParameters) {

    val baseUrl get() = "https://image.tmdb.org/t/p/w500"

    private var idNotification = 0

    private var movie: List<Movie> = ArrayList()

    companion object {
        val TAG: String = NewReleaseMovieWorker::class.java.simpleName

        private const val CHANNEL_ID = "new_release"
        private const val CHANNEL_NAME = "New Release Channel"
        private const val GROUP_KEY = "release_group_key"
        private const val MAX_NOTIFICATION = 5
        private const val NOTIFICATION_REQUEST_CODE = 200
    }

    override fun doWork(): Result {
        return getReleaseMovieToday()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getReleaseMovieToday(): Result {
        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())

        val response =
            movieApi.getMovieReleasedToday(BuildConfig.MOVIEDB_API_KEY, today, today).execute()

        return if (response.isSuccessful) {
            response.body()?.let {
                movie = it.results
                Log.d("Release", "getMovieReleasedToday ${it.results}")

                movie.forEach { _ ->
                    if (idNotification <= MAX_NOTIFICATION){
                        showNotification()
                        idNotification++
                    }
                }
            }
            Result.success()
        } else {
            Result.failure()
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                NOTIFICATION_REQUEST_CODE, Intent(context, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                NOTIFICATION_REQUEST_CODE, Intent(context, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val builder: NotificationCompat.Builder
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (idNotification < MAX_NOTIFICATION) {
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification].title
                    )
                )
                .setContentText(movie[idNotification].overview)
                .setSmallIcon(R.drawable.ic_movie)
                .setGroup(GROUP_KEY)
                .setPriority(PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setAutoCancel(true)

            val posterPath = movie[idNotification].posterPath
            if (posterPath.equals(null) || posterPath == ""){
                val posterMovie = Glide.with(applicationContext)
                    .asBitmap()
                    .load("https://via.placeholder.com/512x256.png?text=${applicationContext.resources.getString(R.string.app_name)}")
                    .submit()

                val bitmap = posterMovie.get()
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
            }else{
                val posterMovie = Glide.with(applicationContext)
                    .asBitmap()
                    .load("${baseUrl}$posterPath")
                    .submit()

                val bitmap = posterMovie.get()
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
            }

        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification].title
                    )
                )
                .addLine(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification - 1].title
                    )
                )
                .addLine(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification - 2].title
                    )
                )
                .addLine(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification - 3].title
                    )
                )
                .addLine(
                    applicationContext.resources.getString(
                        R.string.release_reminder_title,
                        movie[idNotification - 4].title
                    )
                )
                .setBigContentTitle(
                    applicationContext.resources.getString(
                        R.string.new_movie,
                        idNotification
                    )
                )
                .setSummaryText(
                    applicationContext.resources.getString(
                        R.string.new_movie_total,
                        movie.size
                    )
                )

            builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(
                    applicationContext.resources.getString(
                        R.string.new_movie,
                        idNotification
                    )
                )
                .setContentText(applicationContext.resources.getString(R.string.new_movie_today))
                .setSmallIcon(R.drawable.ic_movie)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setAutoCancel(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            builder.setChannelId(CHANNEL_ID)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManager.notify(idNotification, notification)

        Log.d("Notif", "releaseReminder $builder")
    }
}