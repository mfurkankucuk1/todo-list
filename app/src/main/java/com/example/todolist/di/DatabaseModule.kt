package com.example.todolist.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.todolist.data.remote.TodoListDatabaseDao
import com.example.todolist.data.repository.PreferencesRepository
import com.example.todolist.data.repository.TodoListRepository
import com.example.todolist.utils.Constants
import com.example.todolist.utils.Constants.TODO_LIST_DATABASE_NAME
import com.example.todolist.viewModel.TodoListServiceViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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


    @Provides
    fun provideCourierServiceViewModel(
        todoListRepository: TodoListRepository,
        @ApplicationContext context: Context
    ) = TodoListServiceViewModel(
        CoroutineScope(Job()),
        todoListRepository,
        context
    )


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesRepository =
        PreferencesRepository(sharedPreferences)


}