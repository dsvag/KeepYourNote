package com.dsvag.keepyournote.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.data.viewmodels.NoteViewModel
import com.dsvag.keepyournote.databinding.FragmentNoteBinding
import dev.sasikanth.colorsheet.ColorSheet

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, NoteViewModel.Factory(requireNotNull(this.activity).application))
            .get(NoteViewModel::class.java)
    }

    private lateinit var note: Note

    private var isDelete: Boolean = false

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
            painColorIcon(note.color)
        } else {
            note = Note(title = "", description = "")
        }

        binding.description.requestFocus()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        if (!isDelete) {
            saveNote()
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
                findNavController().navigate(R.id.action_noteFragment_to_labelFragment)
                true
            }
            R.id.color -> {
                openColorPicker()
                true
            }
            R.id.share -> {
                if (checkNote()) {
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

    private fun saveNote() {
        val titleText = binding.title.text.toString().trim()
        val descriptionText = binding.description.text.toString().trim()

        note = note.copy(
            title = titleText,
            description = descriptionText,
        )

        if (checkNote()) {
            viewModel.insert(note)
        }
    }

    private fun checkNote() = note.description.isNotEmpty() || note.title.isNotEmpty()

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

    private fun openColorPicker() {
        ColorSheet()
            .colorPicker(
                colors = colors(),
                noColorOption = true,
                listener = { color ->
                    note = note.copy(color = color)
                    painColorIcon(color)
                }
            ).show(requireActivity().supportFragmentManager)
    }

    private fun painColorIcon(color: Int) {
        val drawable = requireContext().getDrawable(R.drawable.ic_baseline_color_lens)!!
        if (color == -1) {
            drawable.setTint(requireContext().getColor(R.color.white))
        } else {
            drawable.setTint(color)
        }
    }

    private fun colors() = intArrayOf(
        requireContext().getColor(R.color.coral_a200),
        requireContext().getColor(R.color.pink_a200),
        requireContext().getColor(R.color.fuchsia_a200),
        requireContext().getColor(R.color.purple_a200),
        requireContext().getColor(R.color.blue_a200),
        requireContext().getColor(R.color.aqua_a200),
        requireContext().getColor(R.color.mint_a200),
        requireContext().getColor(R.color.yellow_a200),
        requireContext().getColor(R.color.orange_a200),
        requireContext().getColor(R.color.brown_a200),
        requireContext().getColor(R.color.grey_a200),
    )
}