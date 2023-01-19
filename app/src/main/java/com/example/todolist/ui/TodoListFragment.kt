package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoListBinding
import com.example.todolist.utils.customNavigate
import com.example.todolist.utils.remove
import com.example.todolist.viewModel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding get() = _binding!!
    private val todoListViewModel: TodoListViewModel by activityViewModels<TodoListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupUI()
        handleClickEvents()
        subscribeObserve()
    }

    private fun setupUI() {
        binding.incHeader.apply {
            tvHeader.text = requireContext().getString(R.string.task_list).uppercase()
            imgClose.remove()
        }
    }

    private fun subscribeObserve() {

    }

    private fun handleClickEvents() {
        binding.apply {
            btnNewTask.setOnClickListener {
                customNavigate(R.id.action_todoListFragment_to_newTaskFragmnet, bundle = null)
            }
        }
    }

    private fun initialize() {

    }
}