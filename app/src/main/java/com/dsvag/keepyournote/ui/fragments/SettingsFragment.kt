package com.dsvag.keepyournote.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.utils.Theme
import com.dsvag.keepyournote.data.viewmodels.NoteViewModel
import com.dsvag.keepyournote.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, NoteViewModel.Factory(requireNotNull(this.activity).application))
            .get(NoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.themeGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.themeLight -> viewModel.setTheme(Theme.LIGHT)
                R.id.themeDark -> viewModel.setTheme(Theme.DARK)
                R.id.themeSystem -> viewModel.setTheme(Theme.SYSTEM)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}