package com.example.todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.data.repository.PreferencesRepository
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.LayoutOverlayBinding
import com.example.todolist.service.OverlayForegroundService
import com.example.todolist.ui.adapter.TodoListAdapter
import com.example.todolist.utils.Constants
import com.example.todolist.viewModel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private var mView: LayoutOverlayBinding? = null
    private var mParams: WindowManager.LayoutParams? = null
    private var mWindowManager: WindowManager? = null
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter(isDoneList = false) }
    private var todoList = ArrayList<TodoListModel>()
    private var countValue = 0
    private var isOpenService = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClickEvents()
        checkOverlayPermission()
        startService()
        setupOverlay()
        registerBroadcast()
    }

    private fun handleClickEvents() {
        todoListAdapter.apply {
            setOnDoneClickListener { currentItem ->
                setupTaskDone(currentItem)
            }
        }
    }

    private fun setupTaskDone(currentItem: TodoListModel) {
        val updateIntent = Intent("UpdateAction")
        updateIntent.putExtra("model",currentItem)
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateIntent)
    }

    private fun registerBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(overlayBroadcast, IntentFilter("Overlay"))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(counterBroadcast, IntentFilter("CounterAction"))
    }

    private fun handleAllTaskListSuccessResponse(result: ArrayList<TodoListModel>) {
        todoList = ArrayList()
        todoList.addAll(result)
        todoListAdapter.list = todoList
    }

    private fun startService() {
        if (!isOpenService) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(Intent(this, OverlayForegroundService::class.java))
                    } else {
                        startService(Intent(this, OverlayForegroundService::class.java))
                    }
                }
            } else {
                startService(Intent(this, OverlayForegroundService::class.java))
            }
        }
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(myIntent)
            }
        }
    }

    private val overlayBroadcast: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val value = intent.getIntExtra(Constants.SEND_BROADCAST_DATA, -1)
            countValue = intent.getIntExtra(Constants.SERVICE_COUNT, -1)
            isOpenService = intent.getBooleanExtra(Constants.IS_OPEN_SERVICE, false)
            todoList = intent.getParcelableArrayListExtra("arrayList")!!
            if (value > -1) {
                handleAllTaskListSuccessResponse(todoList)
                openOverlayScreen()
            }
        }
    }

    private val counterBroadcast: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val value = intent.getIntExtra(Constants.SERVICE_COUNT, -1)
            if (value > 0) {
                countValue = value
            }
        }
    }

    private fun setupOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        mView = LayoutOverlayBinding.inflate(layoutInflater)
        mView?.let { view ->
            view.apply {
                btnCloseOverlay.setOnClickListener {
                    closeOverlayScreen()
                }
                llGeneral.setBackgroundColor(
                    Color.parseColor(
                        preferencesRepository.getStringPreferences(
                            Constants.THEME_COLOR
                        )
                    )
                )

                rvTodoList.adapter = todoListAdapter
            }
        }
        mParams!!.gravity = Gravity.CENTER
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private fun openOverlayScreen() {
        try {
            if (mView!!.root.windowToken == null) {
                if (mView!!.root.parent == null) {
                    mView!!.tvCounter.text = "$countValue ${resources.getString(R.string.active)}"
                    mWindowManager!!.addView(mView!!.root, mParams)
                }
            }
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    private fun closeOverlayScreen() {
        try {
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView!!.root)
            mView!!.root.invalidate()
            (mView!!.root.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }
}