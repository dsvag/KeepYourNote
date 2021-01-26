package com.dsvag.keepyournote.ui.screens.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.FragmentNoteBinding
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.ui.screens.colors.ColorSheet
import com.dsvag.keepyournote.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding by viewBinding(FragmentNoteBinding::bind)

    private val noteViewModel by viewModels<NoteViewModel>()

    private var note: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        note = arguments?.getParcelable("note") ?: Note(title = "", description = "")

        binding.title.setText(note?.title)
        binding.description.setText(note?.description)

        binding.description.requestFocus()

        binding.title.addTextChangedListener {
            note = note?.copy(title = it.toString().trim())
        }

        binding.description.addTextChangedListener {
            note = note?.copy(description = it.toString().trim())
        }
    }

    override fun onStart() {
        super.onStart()
        this.noteViewModel.showKeyBoard(binding.description)
    }

    override fun onPause() {
        super.onPause()
        if (note != null && (note!!.title.isNotEmpty() || note!!.description.isNotEmpty())) {
            this.noteViewModel.insertNote(note!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.toolbar_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.color -> {
                openColorPicker()
                true
            }
            R.id.share -> {
                if (note != null) {
                    shareNote()
                } else {
                    Toast.makeText(requireContext(), "Note is empty", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.delete -> {
                deleteNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openColorPicker() {
        ColorSheet()
            .setOnClickListener { color -> note = note?.copy(color = color) }
            .show(parentFragmentManager, "Colors")
    }

    private fun shareNote() {
        val text = StringBuilder().apply {
            append(binding.title.text.toString().trim())
            append("\n")
            append(binding.description.text.toString().trim())
        }.toString()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        startActivity(Intent.createChooser(sendIntent, "Send via"))
    }

    private fun deleteNote() {
        this.noteViewModel.hideKeyBoard(binding.description)
        this.noteViewModel.deleteNote(note!!)
        note = null
        findNavController().popBackStack()
    }
}