package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.databinding.FragmentDoneListBinding
import com.example.todolist.ui.adapter.TodoListAdapter
import com.example.todolist.utils.show
import com.example.todolist.viewModel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneListFragment : Fragment() {

    private var _binding: FragmentDoneListBinding? = null
    private val binding: FragmentDoneListBinding get() = _binding!!
    private val todoListViewModel: TodoListViewModel by activityViewModels<TodoListViewModel>()
    private val doneListAdapter: TodoListAdapter by lazy { TodoListAdapter(isDoneList = true) }
    private var doneList = ArrayList<TodoListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        populateUI()
        handleClickEvents()
        setupAdapter()
        subscribeObserve()
    }

    private fun handleClickEvents() {
        binding.apply {
            incHeader.imgClose.setOnClickListener {
                setupPopBack()
            }
        }
        doneListAdapter.apply {
            setOnDoneClickListener { item ->
                item.id?.let { todoListViewModel.updateDoneTask(it, isDone = 0) }
            }
            setOnDeleteClickListener { item ->
                setupDeleteTask(item)
            }
        }
    }

    private fun setupPopBack() {
        findNavController().popBackStack()
    }

    private fun subscribeObserve() {
        todoListViewModel.getAllDoneTaskListResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        handleAllDoneTaskListSuccessResponse(result)
                    }
                    todoListViewModel.clearGetAllDoneTaskListResponse()
                }
            }
        }
        todoListViewModel.updateDoneTaskResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        initialize()
                    }
                    todoListViewModel.clearUpdateDoneTaskResponse()
                }
            }
        }

        todoListViewModel.deleteTaskResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        initialize()
                    }
                    todoListViewModel.clearDeleteTaskResponse()
                }
            }
        }
    }

    private fun setupDeleteTask(todoListModel: TodoListModel) {
        todoListViewModel.deleteTask(todoListModel)
    }

    private fun handleAllDoneTaskListSuccessResponse(result: List<TodoListModel>) {
        doneList = ArrayList()
        doneList.addAll(result)
        doneListAdapter.list = doneList
    }

    private fun populateUI() {
        binding.incHeader.apply {
            binding.incHeader.apply {
                tvHeader.text = requireContext().getString(R.string.done_list).uppercase()
                imgClose.show()
            }
        }
    }

    private fun setupAdapter() {
        binding.rvDoneList.adapter = doneListAdapter
    }

    private fun initialize() {
        todoListViewModel.getAllDoneTaskList()
    }

}