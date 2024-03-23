package com.tfg.bibliofinder.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.screens.MainActivity
import com.tfg.bibliofinder.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationTime =
            LocalDateTime.parse(intent?.getStringExtra("bookingTime")).minusMinutes(5L)

        val notificationTimeMillis =
            notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val delay = notificationTimeMillis - System.currentTimeMillis()

        Handler(Looper.getMainLooper()).postDelayed({
            makeBookingNotification()
        }, delay)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun makeBookingNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val yesPendingIntent = PendingIntent.getBroadcast(
            this, 0, Intent(this, YesReceiver::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        val noPendingIntent = PendingIntent.getBroadcast(
            this, 0, Intent(this, NoReceiver::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar_clock)
            .setContentTitle(getString(R.string.booking_notification))
            .setContentText(getString(R.string.you_have_a_booking_will_you_attend))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_door_open, getString(R.string.no_cancel_booking), noPendingIntent
            ).addAction(R.drawable.ic_event_available, getString(R.string.yes), yesPendingIntent)

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

        private val workstationManager: WorkstationManager by inject()

        override fun onReceive(context: Context?, intent: Intent?) {
            CoroutineScope(Dispatchers.IO).launch {
                workstationManager.releaseWorkstation()
            }

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }
    }
}