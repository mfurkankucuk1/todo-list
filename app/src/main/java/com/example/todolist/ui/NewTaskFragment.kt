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
import com.example.todolist.databinding.FragmentNewTaskBinding
import com.example.todolist.utils.Constants
import com.example.todolist.utils.getCurrentTime
import com.example.todolist.utils.show
import com.example.todolist.viewModel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding: FragmentNewTaskBinding get() = _binding!!
    private val todoListViewModel: TodoListViewModel by activityViewModels<TodoListViewModel>()
    private var title: String? = null
    private var description: String? = null
    private var taskId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        val taskId = arguments?.getInt(Constants.TASK_ID_BUNDLE_KEY)
        val title = arguments?.getString(Constants.TASK_TITLE_BUNDLE_KEY)
        val description = arguments?.getString(Constants.TASK_TITLE_BUNDLE_KEY)

        if (taskId != null) {
            this.taskId = taskId
        }
        if (title != null) {
            this.title = title
        }
        if (description != null) {
            this.description = description
        }
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
            taskId?.let { id ->
                tvHeader.text = requireContext().getString(R.string.update_task).uppercase()
            } ?: run {
                tvHeader.text = requireContext().getString(R.string.new_task).uppercase()
            }
            imgClose.show()
        }
        binding.apply {
            title?.let { _title ->
                etTitle.setText(_title)
            }
            description?.let { _description ->
                etDescription.setText(_description)
            }
            taskId?.let { id ->
                btnAdd.text = requireContext().getString(R.string.update)
            }
        }
    }

    private fun subscribeObserve() {
        todoListViewModel.addTodoResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    response?.data?.let { result ->
                        handleAddTaskSuccessResponse(result)
                    }
                    todoListViewModel.clearAddTaskResponse()
                }
            }
        }

    }

    private fun handleAddTaskSuccessResponse(result: Long) {
        if (result > 0) {
            setupPopBack()
        }
    }

    private fun handleClickEvents() {
        binding.apply {
            btnCancel.setOnClickListener {
                setupPopBack()
            }
            incHeader.imgClose.setOnClickListener {
                setupPopBack()
            }
            btnAdd.setOnClickListener {
                setupAddTask(binding.etTitle.text.toString(), binding.etDescription.text.toString())
            }
        }
    }

    private fun setupAddTask(title: String, description: String) {
        taskId?.let { id ->
            todoListViewModel.updateTask(id, title, description)
        } ?: run {
            val todoListModel = TodoListModel(
                null,
                title = title,
                description = description,
                getCurrentTime(),
                null,
                false
            )
            todoListViewModel.addTodo(todoListModel = todoListModel)
        }

    }

    private fun setupPopBack() {
        findNavController().popBackStack()
    }

    private fun initialize() {

    }


}