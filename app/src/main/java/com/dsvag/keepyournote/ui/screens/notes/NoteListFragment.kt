package com.dsvag.keepyournote.ui.screens.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.FragmentNoteListBinding
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.utils.recyclerviewUtils.ItemDecoration
import com.dsvag.keepyournote.utils.recyclerviewUtils.SwipeCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    private val noteAdapter by lazy { NoteAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerview()

        binding.newButton.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_noteFragment)
        }

        noteViewModel.getNotes.observe(viewLifecycleOwner) { newNoteList ->
            newNoteList.let {
                noteAdapter.setData(newNoteList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.recyclerview.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.recyclerview.addItemDecoration(ItemDecoration(8))
        binding.recyclerview.adapter = noteAdapter

        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.newButton.hide()
                } else if (dy < 0) {
                    binding.newButton.show()
                }
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