package com.example.todolist.data.repository

import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.data.remote.TodoListDatabaseDao
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoListRepository @Inject constructor(
    private val todoListDatabaseDao: TodoListDatabaseDao
) {

    suspend fun addTodo(todoListModel: TodoListModel) = flow {
        emit(Resource.Loading(true))
        val addTodoResponse = todoListDatabaseDao.insert(todoListModel)
        emit(Resource.Success(addTodoResponse))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

}