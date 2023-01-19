package com.example.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoListModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdAt") var createdAt:String,
    @ColumnInfo(name = "updateAt") var updateAt:String?=null,
    @ColumnInfo(name = "isDone") var  isDone:Boolean,

)
