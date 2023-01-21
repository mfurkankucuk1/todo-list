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


    /**
     * Add task to db
     * **/
    suspend fun addTodo(todoListModel: TodoListModel) = flow {
        emit(Resource.Loading(true))
        val addTodoResponse = todoListDatabaseDao.insert(todoListModel)
        emit(Resource.Success(addTodoResponse))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

    /**
     * Get all task list in db
     * **/
    suspend fun getAlLTodoList() = flow {
        emit(Resource.Loading(true))
        val getAllTodoList = todoListDatabaseDao.getAllTodoList()
        emit(Resource.Success(getAllTodoList))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

    /**
     * Delete task in db
     * **/

    suspend fun deleteTask(todoListModel: TodoListModel) = flow {
        emit(Resource.Loading(true))
        val getAllTodoList = todoListDatabaseDao.deleteTask(todo = todoListModel)
        emit(Resource.Success(getAllTodoList))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

    /**
     * Update done
     * **/
    suspend fun updateTaskDone(id: Int, isDone: Int) = flow {
        emit(Resource.Loading(true))
        val getAllTodoList = todoListDatabaseDao.updateTakeDone(taskId = id, isDoneValue = isDone)
        emit(Resource.Success(getAllTodoList))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

    /**
     * Update task
     * **/
    suspend fun updateTask(id: Int, title: String, description: String) = flow {
        emit(Resource.Loading(true))
        val getAllTodoList =
            todoListDatabaseDao.updateTask(taskId = id, title = title, description = description)
        emit(Resource.Success(getAllTodoList))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

    /**
     * Get all done task list
     * **/
    suspend fun getTaskDoneList() = flow {
        emit(Resource.Loading(true))
        val getAllDoneList =
            todoListDatabaseDao.getTaskDoneList()
        emit(Resource.Success(getAllDoneList))
    }.catch { e ->
        emit(Resource.Error(e.message.toString()))
    }

}