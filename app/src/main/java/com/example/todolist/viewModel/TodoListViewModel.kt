package com.example.todolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.data.repository.TodoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    application: Application,
    private val todoListRepository: TodoListRepository
) : AndroidViewModel(application) {

    /**
     * Add task
     **/

    private var _addTodoResponse: MutableLiveData<Resource<Long>?> = MutableLiveData()
    val addTodoResponse: LiveData<Resource<Long>?> get() = _addTodoResponse

    fun clearAddTaskResponse() {
        _addTodoResponse.postValue(null)
    }

    fun addTodo(todoListModel: TodoListModel) {
        addTodoSafeCall(todoListModel)
    }

    private fun addTodoSafeCall(todoListModel: TodoListModel) {
        viewModelScope.launch {
            todoListRepository.addTodo(todoListModel).collect {
                it.let {
                    _addTodoResponse.postValue(it)
                }
            }
        }
    }

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
     * Delete task
     * **/

    private var _deleteTaskResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val deleteTaskResponse: LiveData<Resource<Int>?> get() = _deleteTaskResponse

    fun clearDeleteTaskResponse() {
        _deleteTaskResponse.postValue(null)
    }

    fun deleteTask(todoListModel: TodoListModel) {
        deleteTaskSafeCall(todoListModel)
    }

    private fun deleteTaskSafeCall(todoListModel: TodoListModel) {
        viewModelScope.launch {
            todoListRepository.deleteTask(todoListModel).collect() {
                it.let {
                    _deleteTaskResponse.postValue(it)
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
        viewModelScope.launch {
            todoListRepository.updateTaskDone(id = id, isDone = isDone).collect() {
                it.let {
                    _updateDoneTaskForActivityResponse.postValue(it)
                }
            }
        }
    }

    /**
     * Update task
     * **/

    private var _updateTaskResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val updateTaskResponse: LiveData<Resource<Int>?> get() = _updateTaskResponse

    fun clearUpdateTaskResponse() {
        _updateDoneTaskResponse.postValue(null)
    }

    fun updateTask(id: Int, title: String, description: String) {
        updateTaskSafeCall(id, title, description)
    }

    private fun updateTaskSafeCall(id: Int, title: String, description: String) {
        viewModelScope.launch {
            todoListRepository.updateTask(id, title, description).collect() {
                it.let {
                    _updateTaskResponse.postValue(it)
                }
            }
        }
    }

    /**
     * All done task list
     * **/

    private var _getAllDoneTaskListResponse: MutableLiveData<Resource<List<TodoListModel>>?> =
        MutableLiveData()
    val getAllDoneTaskListResponse: LiveData<Resource<List<TodoListModel>>?> get() = _getAllDoneTaskListResponse

    fun clearGetAllDoneTaskListResponse() {
        _getAllDoneTaskListResponse.postValue(null)
    }

    fun getAllDoneTaskList() {
        getAllDoneTaskListSafeCall()
    }

    private fun getAllDoneTaskListSafeCall() {
        viewModelScope.launch {
            todoListRepository.getTaskDoneList().collect() {
                it.let {
                    _getAllDoneTaskListResponse.postValue(it)
                }
            }
        }
    }

}