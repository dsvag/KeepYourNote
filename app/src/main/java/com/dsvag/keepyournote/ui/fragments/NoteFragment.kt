package com.dsvag.keepyournote.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.data.viewmodels.NoteViewModel
import com.dsvag.keepyournote.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, NoteViewModel.Factory(requireNotNull(this.activity).application))
            .get(NoteViewModel::class.java)
    }

    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        val maybeNote = this.arguments?.getSerializable("note")

        if (maybeNote != null) {
            note = maybeNote as Note
            binding.title.setText(note.title)
            binding.description.setText(note.description)
        } else {
            note = Note(title = "", description = "")
        }

        binding.description.requestFocus()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        saveNote()
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
            R.id.share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, binding.title.text.toString().trim())
                sendIntent.putExtra(Intent.EXTRA_TEXT, binding.description.text.toString().trim())
                sendIntent.type = "text/plain"
                Intent.createChooser(sendIntent, "Send via")
                startActivity(sendIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveNote() {
        val titleText = binding.title.text.toString().trim()
        val descriptionText = binding.description.text.toString().trim()

        note = note.copy(title = titleText, description = descriptionText)

        if (note.description.isNotEmpty() || note.title.isNotEmpty()) {
            viewModel.insert(note)
        }
    }
}