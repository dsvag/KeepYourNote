package com.dsvag.keepyournote.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.adapters.SwipeCallback
import com.dsvag.keepyournote.data.adapters.note.NoteAdapter
import com.dsvag.keepyournote.data.adapters.note.NoteDecoration
import com.dsvag.keepyournote.data.adapters.note.NoteDiffUtilCallback
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.data.viewmodels.NoteViewModel
import com.dsvag.keepyournote.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, NoteViewModel.Factory(requireNotNull(this.activity).application)
        ).get(NoteViewModel::class.java)
    }

    private val noteAdapter by lazy { NoteAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        initRecyclerview()

        binding.newButton.setOnClickListener {
            findNavController().navigate(R.id.noteFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getNotes.observe(viewLifecycleOwner) { newNoteList: List<Note>? ->
            if (newNoteList != null) {
                noteAdapter.setData(
                    newNoteList, DiffUtil.calculateDiff(
                        NoteDiffUtilCallback(noteAdapter.getData(), newNoteList)
                    )
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.choseLayout -> {
                changeLayout(item)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerview() {
        val noteDecoration = NoteDecoration(10)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete)!!
        Log.d("Drawable", drawable.toString())

        val itemTouchHelper = ItemTouchHelper(
            SwipeCallback(
                ::deleteNote,
                drawable,
                ContextCompat.getColor(requireContext(), R.color.error_red),
            )
        )

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.recyclerview.addItemDecoration(noteDecoration)
        binding.recyclerview.adapter = noteAdapter

        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.newButton.visibility == View.VISIBLE) {
                    binding.newButton.hide()
                } else if (dy < 0 && binding.newButton.visibility != View.VISIBLE) {
                    binding.newButton.show()
                }
            }
        })
    }

    private fun deleteNote(position: Int) {
        val note = noteAdapter.getItem(position)
        viewModel.deleteNote(note)
    }

    private fun changeLayout(item: MenuItem) {
        if (binding.recyclerview.layoutManager == StaggeredGridLayoutManager(2, 1)) {
            binding.recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu)
        } else {
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
            item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_grid)
        }
    }
}