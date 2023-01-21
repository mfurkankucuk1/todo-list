package com.example.todolist.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.todolist.R
import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.utils.Constants
import com.example.todolist.utils.Constants.IS_OPEN_SERVICE
import com.example.todolist.utils.Constants.SERVICE_COUNT
import com.example.todolist.viewModel.TodoListServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class OverlayForegroundService : LifecycleService() {

    private var count = 0

    @Inject
    lateinit var serviceViewModel: TodoListServiceViewModel
    private var serviceList = ArrayList<TodoListModel>()


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )
        registerUpdateBroadcast()

    }

    private fun registerUpdateBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                messageReceiver,
                IntentFilter("UpdateAction")
            )
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val model = intent.getParcelableExtra<TodoListModel>("model")
            if (model!=null){
                serviceViewModel.updateDoneForActivityTask(model.id!!,1)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleObservers()
        val t = Thread {
            while (true) {
                startCounter()
                Thread.sleep(1000)
            }
        }
        t.start()
        return START_NOT_STICKY
    }

    private fun handleObservers() {
        serviceViewModel.getAllTaskListForActivityResponse.observeForever { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        handleAllTaskListSuccessResponse(result)
                    }
                    serviceViewModel.clearGetAllTaskListForActivityResponse()
                }
            }
        }

        serviceViewModel.updateDoneTaskForActivityResponse.observeForever { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                         sendMessageToActivity()
                    }
                    serviceViewModel.clearUpdateDoneTaskForActivityResponse()
                }
            }
        }

    }

    private fun handleAllTaskListSuccessResponse(result: List<TodoListModel>) {
        serviceList = ArrayList()
        serviceList.addAll(result)
        val intent = Intent("Overlay")
        intent.putExtra(Constants.SEND_BROADCAST_DATA, 1)
        intent.putExtra(SERVICE_COUNT, startCounter())
        intent.putExtra(IS_OPEN_SERVICE, true)
        intent.putParcelableArrayListExtra("arrayList", serviceList)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    private fun startCounter(): Int {
        count += 1
        return count
    }


    private fun sendMessageToActivity() {
        serviceViewModel.getAllTaskListForActivity()
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