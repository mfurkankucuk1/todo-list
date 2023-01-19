package com.example.todolist.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.remote.TodoListDatabaseDao
import com.example.todolist.data.model.TodoListModel

@Database(entities = [TodoListModel::class], version = 1)
abstract class TodoListDatabase:RoomDatabase(){
    abstract fun getTodoListDao() : TodoListDatabaseDao
}