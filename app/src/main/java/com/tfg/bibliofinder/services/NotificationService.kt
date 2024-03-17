package com.tfg.bibliofinder.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.screens.activities.MainActivity
import com.tfg.bibliofinder.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationService(private val context: Context) {

    fun scheduleNotification(notificationTime: LocalDateTime) {
        val notificationTimeMillis =
            notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val delay = notificationTimeMillis - System.currentTimeMillis()

        Handler(Looper.getMainLooper()).postDelayed({
            makeBookingNotification()
        }, delay)
    }

    private fun makeBookingNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val yesPendingIntent = PendingIntent.getBroadcast(
            context, 0, Intent(context, YesReceiver::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        val noPendingIntent = PendingIntent.getBroadcast(
            context, 0, Intent(context, NoReceiver::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar_clock).setContentTitle("Booking notification")
            .setContentText("You have a booking, will you attend?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_door_open, "No, cancel booking", noPendingIntent)
            .addAction(R.drawable.ic_event_available, "Yes", yesPendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }

    class YesReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }
    }

    class NoReceiver : BroadcastReceiver(), KoinComponent {

        private val workstationService: WorkstationService by inject()

        override fun onReceive(context: Context?, intent: Intent?) {
            CoroutineScope(Dispatchers.IO).launch {
                workstationService.releaseWorkstation()
            }

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }
    }
}