package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.R
import com.example.todolist.data.model.Resource
import com.example.todolist.data.model.TodoListModel
import com.example.todolist.databinding.FragmentTodoListBinding
import com.example.todolist.ui.adapter.TodoListAdapter
import com.example.todolist.utils.Constants.TASK_DESCRIPTION_BUNDLE_KEY
import com.example.todolist.utils.Constants.TASK_ID_BUNDLE_KEY
import com.example.todolist.utils.Constants.TASK_TITLE_BUNDLE_KEY
import com.example.todolist.utils.customNavigate
import com.example.todolist.utils.remove
import com.example.todolist.viewModel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding get() = _binding!!
    private val todoListViewModel: TodoListViewModel by activityViewModels<TodoListViewModel>()
    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter(isDoneList = false,isOverlay = false) }
    private var todoList = ArrayList<TodoListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetAllTaskList()
        setupAdapter()
        setupUI()
        handleClickEvents()
        subscribeObserve()
    }

    private fun setupGetAllTaskList() {
        todoListViewModel.getAllTaskList()
    }

    private fun setupAdapter() {
        binding.rvTodoList.apply { adapter = todoListAdapter }

    }

    private fun setupUI() {
        binding.incHeader.apply {
            tvHeader.text = requireContext().getString(R.string.task_list).uppercase()
            imgClose.remove()
        }
    }

    private fun subscribeObserve() {
        todoListViewModel.getAllTaskListResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        handleAllTaskListSuccessResponse(result)
                    }
                    todoListViewModel.clearGetAllTaskListResponse()
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
                        setupGetAllTaskList()
                    }
                    todoListViewModel.clearDeleteTaskResponse()
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
                        setupGetAllTaskList()
                    }
                    todoListViewModel.clearUpdateDoneTaskResponse()
                }
            }
        }
    }


    private fun handleAllTaskListSuccessResponse(result: List<TodoListModel>) {
        todoList = ArrayList()
        todoList.addAll(result)
        todoListAdapter.list = todoList
    }

    private fun handleClickEvents() {
        binding.apply {
            btnNewTask.setOnClickListener {
                setupNavigateDetail(currentItem = null, isUpdate = false)
            }
            btnDoneList.setOnClickListener {
                setupNavigateDoneList()
            }
            btnSettings.setOnClickListener {
                setupNavigateSettings()
            }
        }
        todoListAdapter.apply {
            setOnDeleteClickListener { currentItem ->
                setupDeleteTask(currentItem)
            }
            setOnUpdateClickListener { currentItem ->
                setupNavigateDetail(currentItem = currentItem, isUpdate = true)
            }
            setOnDoneClickListener { currentItem ->
                setupTaskDone(currentItem)
            }
        }
    }

    private fun setupNavigateSettings() {
        customNavigate(R.id.action_todoListFragment_to_settingsFragment, null)
    }

    private fun setupNavigateDoneList() {
        customNavigate(R.id.action_todoListFragment_to_doneListFragment, null)
    }

    private fun setupTaskDone(currentItem: TodoListModel) {
        currentItem.id?.let { todoListViewModel.updateDoneTask(it, isDone = 1) }
    }

    private fun setupNavigateDetail(currentItem: TodoListModel?, isUpdate: Boolean) {
        if (isUpdate) {
            val bundle = Bundle().apply {
                currentItem?.id?.let { putInt(TASK_ID_BUNDLE_KEY, it) }
                putString(TASK_TITLE_BUNDLE_KEY, currentItem?.title)
                putString(TASK_DESCRIPTION_BUNDLE_KEY, currentItem?.description)
            }
            customNavigate(R.id.action_todoListFragment_to_newTaskFragmnet, bundle = bundle)
        } else {
            customNavigate(R.id.action_todoListFragment_to_newTaskFragmnet, bundle = null)
        }
    }

    private fun setupDeleteTask(todoListModel: TodoListModel) {
        todoListViewModel.deleteTask(todoListModel)
    }
}