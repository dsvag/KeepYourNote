package com.dsvag.keepyournote.ui.notes

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.FragmentNoteListBinding
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.ui.viewBinding
import com.dsvag.keepyournote.utils.recyclerview.ItemDecoration
import com.dsvag.keepyournote.utils.recyclerview.SwipeCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)

    private val noteViewModel by viewModels<NoteViewModel>()

    private val noteAdapter by lazy { NoteAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initRecyclerview()

        (activity as AppCompatActivity?)?.supportActionBar?.show()

        binding.newButton.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_noteFragment)
        }

        noteViewModel.notes.observe(viewLifecycleOwner) { newNoteList: List<Note>? ->
            newNoteList?.let { noteAdapter.setData(newNoteList) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel.initDatabase()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {
                noteViewModel.singOut()
                findNavController().navigate(R.id.action_noteListFragment_to_loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerview() {
        val itemTouchHelper = ItemTouchHelper(
            SwipeCallback(
                ::deleteNote,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!,
                requireContext().getColor(R.color.error_red),
            )
        )

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.addItemDecoration(ItemDecoration(32f))
        binding.recyclerview.adapter = noteAdapter

        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                binding.newButton.isVisible = dy >= 0
            }
        })
    }

    private fun deleteNote(position: Int) {
        val note = noteAdapter.getItem(position)
        noteViewModel.deleteNote(note)
        showDeleteSnackbar(note)
    }

    private fun showDeleteSnackbar(note: Note) {
        Snackbar
            .make(binding.root, "Note has deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") { noteViewModel.insertNote(note) }
            .show()
    }
}