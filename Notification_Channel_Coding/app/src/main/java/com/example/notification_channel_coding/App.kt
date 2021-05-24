package com.example.notification_channel_coding

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    companion object {
        val CHANNEL_1_ID = "channel1"
        val CHANNEL_2_ID = "channel2"
    }

    override fun onCreate() {
        super.onCreate()


        /**
         * Below the Android oreo version we can directly created the Notification without any Notification channel but
         * from the oreo we have to create our own channel and then have to register with the NotificationManager
         *  But note here will be use NotificationManager instead of NotificationManagerCompat bez this function is not available there
         */
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //This is first channel
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "channel 1",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "This is channel 1"
            }

            //This is second channel
            val channel2 = NotificationChannel(
                CHANNEL_2_ID,
                "channel 2",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is channel 2"
            }

            // Register the channel with the system
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }
}