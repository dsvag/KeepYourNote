package com.dsvag.keepyournote.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.adapters.label.LabelAdapter
import com.dsvag.keepyournote.data.models.Label
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.data.viewmodels.NoteViewModel
import com.dsvag.keepyournote.databinding.FragmentNoteBinding
import com.dsvag.keepyournote.ui.sheets.ColorSheet

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, NoteViewModel.Factory(requireNotNull(this.activity).application))
            .get(NoteViewModel::class.java)
    }

    private val labelAdapter by lazy { LabelAdapter() }

    private lateinit var note: Note

    private var isDelete: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)



        initRv()

        binding.description.requestFocus()

        binding.title.addTextChangedListener {
            note.title = it.toString().trim()
        }

        binding.description.addTextChangedListener {
            note.description = it.toString().trim()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.keyBoardUtils.showKeyBoard(binding.description)
    }

    override fun onPause() {
        super.onPause()
        if (!isDelete && note.isNotEmpty()) {
            viewModel.insertNote(note)
        } else {
            viewModel.deleteNote(note)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.toolbar_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.label -> {
                openLabelsPicker()
                true
            }
            R.id.color -> {
                openColorPicker()
                true
            }
            R.id.share -> {
                if (note.isNotEmpty()) {
                    shareNote()
                } else {
                    Toast.makeText(requireContext(), "Note empty", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.delete -> {
                isDelete = true
                viewModel.deleteNote(note)
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRv() {
        binding.labels.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.labels.setHasFixedSize(false)
        binding.labels.adapter = labelAdapter
        labelAdapter.setIsInNote(false)
    }

    private fun checkArguments() {

        val maybeNote = this.arguments?.getSerializable("note")
        val maybeLabels = this.arguments?.getSerializable("labels")

        if (maybeNote != null) {
            note = maybeNote as Note
            binding.title.setText(note.title)
            binding.description.setText(note.description)
            labelAdapter.setData(note.labels)
        } else {
            note = Note(title = "", description = "")
        }

        if (maybeLabels != null) {
            note.labels.clear()
            note.labels.addAll(maybeLabels as List<Label>)
            labelAdapter.setData(note.labels)
        }
    }

    private fun shareNote() {
        val text = StringBuilder().apply {
            append(binding.title.text.toString().trim())
            append("\n")
            append(binding.description.text.toString().trim())
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text.toString())
            type = "text/plain"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        startActivity(Intent.createChooser(sendIntent, "Send via"))
    }

    private fun openLabelsPicker() {
//        add bundle with note labels
//        findNavController().navigate(R.id.action_noteFragment_to_labelPickFragment)
    }

    private fun openColorPicker() {
        ColorSheet()
            .setOnClickListener { color -> note.color = color }
            .show(parentFragmentManager, "Colors")
    }
}