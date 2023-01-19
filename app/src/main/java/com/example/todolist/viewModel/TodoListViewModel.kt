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

    fun clearGetAllTaskListResponse() {
        _getAllTaskListResponse.postValue(null)
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

    fun clearUpdateDoneTaskResponse() {
        _updateDoneTaskResponse.postValue(null)
    }

    fun updateDoneTask(id: Int, isDone: Int) {
        updateDoneTaskSafeCall(id = id, isDone = isDone)
    }

    private fun updateDoneTaskSafeCall(id: Int, isDone: Int) {
        viewModelScope.launch {
            todoListRepository.updateTakeDone(id = id, isDone = isDone).collect() {
                it.let {
                    _updateDoneTaskResponse.postValue(it)
                }
            }
        }
    }

}