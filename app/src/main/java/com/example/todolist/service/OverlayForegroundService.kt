package com.example.todolist.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.utils.Constants
import com.example.todolist.utils.Constants.IS_OPEN_SERVICE
import com.example.todolist.utils.Constants.SERVICE_COUNT


class OverlayForegroundService : Service() {

    private var count = 0

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY
    }


    private fun sendMessageToActivity() {
        val intent = Intent("Overlay")
        intent.putExtra(Constants.SEND_BROADCAST_DATA, 1)
        intent.putExtra(SERVICE_COUNT, count)
        intent.putExtra(IS_OPEN_SERVICE,true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "OverlayNotification"
        val channelName = "OverlayNotificationChannelName"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        val remoteviews = RemoteViews(this.packageName, R.layout.layout_notification)
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                sendMessageToActivity()
            }
        }, IntentFilter("ServiceBroadcast"))
        val pi = PendingIntent.getBroadcast(
            this, 0,
            Intent("ServiceBroadcast"), 0
        )
        remoteviews.setOnClickPendingIntent(R.id.btnShowTaskList, pi)

        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContent(remoteviews)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        startForeground(2, builder.build())
    }

}