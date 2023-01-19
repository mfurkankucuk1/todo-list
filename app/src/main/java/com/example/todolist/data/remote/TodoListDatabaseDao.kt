package com.example.todolist.data.remote

import androidx.room.*
import com.example.todolist.data.model.TodoListModel

@Dao
interface TodoListDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoListModel): Long

    @Delete
    suspend fun deleteTask(todo: TodoListModel): Int

    @Query("SELECT * FROM TodoListModel")
    suspend fun getAllTodoList(): List<TodoListModel>

    @Query("UPDATE TodoListModel SET isDone =:isDoneValue WHERE id =:taskId")
    suspend fun updateTakeDone(taskId:Int,isDoneValue: Int): Int

    @Query("SELECT * FROM TodoListModel WHERE isDone = 1")
    suspend fun getTaskDoneList(): List<TodoListModel>

    @Query("UPDATE TodoListModel SET title =:title , description=:description WHERE id=:taskId")
    suspend fun updateTask(taskId: Int, title: String, description: String): Int


}