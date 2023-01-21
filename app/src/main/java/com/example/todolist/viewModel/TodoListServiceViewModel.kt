package com.example.todolist.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.data.repository.TodoListRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TodoListServiceViewModel @Inject constructor(
    val viewModelScope:CoroutineScope,
    val todoListRepository: TodoListRepository,
    @ApplicationContext context:Context
) {

    /**
     * Gel all task list
     * **/

    private var _getAllTaskListResponse: MutableLiveData<Resource<List<TodoListModel>>?> =
        MutableLiveData()
    val getAllTaskListResponse: LiveData<Resource<List<TodoListModel>>?> get() = _getAllTaskListResponse

    private var _getAllTaskListForActivityResponse: MutableLiveData<Resource<List<TodoListModel>>?> =
        MutableLiveData()
    val getAllTaskListForActivityResponse: LiveData<Resource<List<TodoListModel>>?> get() = _getAllTaskListForActivityResponse

    fun clearGetAllTaskListResponse() {
        _getAllTaskListResponse.postValue(null)
    }

    fun clearGetAllTaskListForActivityResponse() {
        _getAllTaskListForActivityResponse.postValue(null)
    }

    fun getAllTaskList() {
        getAllTaskListSafeCall()
    }

    private fun getAllTaskListSafeCall() {
        viewModelScope.launch {
            todoListRepository.getAlLTodoList().collect() {
                it.let {
                    _getAllTaskListResponse.postValue(it)
                }
            }
        }
    }

    fun getAllTaskListForActivity() {
        getAllTaskListForActivitySafeCall()
    }

    private fun getAllTaskListForActivitySafeCall() {
        viewModelScope.launch {
            todoListRepository.getAlLTodoList().collect() {
                it.let {
                    _getAllTaskListForActivityResponse.postValue(it)
                }
            }
        }
    }


    /**
     * Update done task
     * **/

    private var _updateDoneTaskResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val updateDoneTaskResponse: LiveData<Resource<Int>?> get() = _updateDoneTaskResponse

    private var _updateDoneTaskForActivityResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val updateDoneTaskForActivityResponse: LiveData<Resource<Int>?> get() = _updateDoneTaskForActivityResponse

    fun clearUpdateDoneTaskResponse() {
        _updateDoneTaskResponse.postValue(null)
    }

    fun clearUpdateDoneTaskForActivityResponse() {
        _updateDoneTaskForActivityResponse.postValue(null)
    }

    fun updateDoneTask(id: Int, isDone: Int) {
        updateDoneTaskSafeCall(id = id, isDone = isDone)
    }

    private fun updateDoneTaskSafeCall(id: Int, isDone: Int) {
        viewModelScope.launch {
            todoListRepository.updateTaskDone(id = id, isDone = isDone).collect() {
                it.let {
                    _updateDoneTaskResponse.postValue(it)
                }
            }
        }
    }

    fun updateDoneForActivityTask(id: Int, isDone: Int) {
        updateDoneForActivityTaskSafeCall(id = id, isDone = isDone)
    }

    private fun updateDoneForActivityTaskSafeCall(id: Int, isDone: Int) {
        Timber.e("Girdi1")
        viewModelScope.launch {
            Timber.e("Girdi2")
            todoListRepository.updateTaskDone(id = id, isDone = isDone).collect() {
                Timber.e("Girdi3")
                it.let {
                    Timber.e("Girdi4")
                    _updateDoneTaskForActivityResponse.postValue(it)
                }
            }
        }
    }


}