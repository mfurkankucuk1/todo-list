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

    private var _addTodoResponse: MutableLiveData<Resource<Long>?> = MutableLiveData()
    val addTodoResponse: LiveData<Resource<Long>?> get() = _addTodoResponse

    fun clearAddTaskResponse(){
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


}