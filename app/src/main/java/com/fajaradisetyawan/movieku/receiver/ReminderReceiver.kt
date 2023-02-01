/*
 * Created by Fajar Adi Setyawan on 23/1/2023 - 11:12:34
 * fajaras465@gmail.com
 * Copyright (c) 2023.
 */

package com.fajaradisetyawan.movieku.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fajaradisetyawan.movieku.R
import com.fajaradisetyawan.movieku.feature.ui.MainActivity
import java.util.*


class ReminderReceiver: BroadcastReceiver(){

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "reminder_apps"
        const val CHANNEL_NAME = "Remainder open apps"

        val TAG: String = ReminderReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        val title = context?.getString(R.string.app_name)
        val message = context?.getString(R.string.reminder_msg_notif)
        val idNotification = NOTIFICATION_ID

        showNotification(context, title, message, idNotification)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(context: Context?, title: String?, message: String?, notifId: Int) {

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0, Intent(context, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                0, Intent(context, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val     notificationManagerCompat = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_movie)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(ContextCompat.getColor(context, android.R.color.black))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)

        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setRepeatingAlarm(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 8)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        pendingIntent.cancel()



        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)

        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE) != null
    }
}