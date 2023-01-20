package com.example.todolist.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.databinding.ItemRowTodoListBinding
import com.example.todolist.utils.remove

class TodoListAdapter constructor(private val isDoneList: Boolean) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    inner class TodoListViewHolder(val binding: ItemRowTodoListBinding) :
        RecyclerView.ViewHolder(binding.root)

    var list = ArrayList<TodoListModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoListViewHolder {

        return TodoListViewHolder(
            ItemRowTodoListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TodoListViewHolder,
        position: Int
    ) {
        val currentItem = list[position]
        holder.binding.apply {
            tvTitle.text = currentItem.title
            tvDescription.text = currentItem.description
            handleClickEvents(holder, currentItem)
            if (isDoneList) {
                imgEdit.remove()
                btnDone.text =
                    holder.itemView.context.resources.getString(R.string.move_to_task_list)
                btnDone.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.move_to_task_list_button_color
                    )
                )
            } else {
                btnDone.text =
                    holder.itemView.context.resources.getString(R.string.done)
                btnDone.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.done_button_color
                    )
                )
            }
        }
    }

    private fun handleClickEvents(
        holder: TodoListViewHolder,
        currentItem: TodoListModel
    ) {
        holder.binding.apply {
            imgRemove.setOnClickListener {
                onClickDeleteListener?.let {
                    it(currentItem)
                }
            }
            imgEdit.setOnClickListener {
                onUpdateClickListener?.let {
                    it(currentItem)
                }
            }
            btnDone.setOnClickListener {
                onDoneClickListener?.let { it(currentItem) }
            }
        }
    }

    override fun getItemCount() = list.size

    private var onClickDeleteListener: ((TodoListModel) -> Unit?)? = null

    fun setOnDeleteClickListener(listener: ((TodoListModel) -> Unit?)) {
        onClickDeleteListener = listener
    }

    private var onUpdateClickListener: ((TodoListModel) -> Unit?)? = null

    fun setOnUpdateClickListener(listener: ((TodoListModel) -> Unit?)) {
        onUpdateClickListener = listener
    }

    private var onDoneClickListener: ((TodoListModel) -> Unit?)? = null

    fun setOnDoneClickListener(listener: ((TodoListModel) -> Unit?)) {
        onDoneClickListener = listener
    }


}