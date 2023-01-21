package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.repository.PreferencesRepository
import com.example.todolist.databinding.FragmentSettingsBinding
import com.example.todolist.utils.Constants
import com.example.todolist.utils.show
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()
        handleClickEvents()
    }

    private fun handleClickEvents() {
        binding.apply {
            incHeader.imgClose.setOnClickListener {
                setupPopBack()
            }
        }
    }

    private fun setupPopBack() {
        findNavController().popBackStack()
    }

    private fun populateUI() {
        binding.incHeader.apply {
            tvHeader.text = requireContext().getString(R.string.settings)
            imgClose.show()
        }

        binding.colorPicker.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
            Timber.e(envelope.hexCode)
            binding.imgThemeColor.setBackgroundColor(envelope.color)
            preferencesRepository.setStringPreferences(
                Constants.THEME_COLOR,
                "#${envelope.hexCode}"
            )
        })
    }

}