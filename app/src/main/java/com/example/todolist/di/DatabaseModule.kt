package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.remote.TodoListDatabaseDao
import com.example.todolist.utils.Constants.TODO_LIST_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): TodoListDatabase {

        val dbBuilder = Room.databaseBuilder(
            context, TodoListDatabase::class.java, TODO_LIST_DATABASE_NAME
        )
        return dbBuilder.allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideProductDao(appDatabase: TodoListDatabase): TodoListDatabaseDao {
        return appDatabase.getTodoListDao()
    }


}