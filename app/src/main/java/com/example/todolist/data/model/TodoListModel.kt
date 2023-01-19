package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoListModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
)
