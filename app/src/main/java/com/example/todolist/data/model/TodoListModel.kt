package com.example.todolist.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class TodoListModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdAt") var createdAt:String,
    @ColumnInfo(name = "updateAt") var updateAt:String?=null,
    @ColumnInfo(name = "isDone") var  isDone:Boolean,
): Parcelable