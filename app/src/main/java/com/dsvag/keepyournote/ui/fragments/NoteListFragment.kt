package com.dsvag.keepyournote.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.adapters.SwipeToDeleteCallback
import com.dsvag.keepyournote.data.adapters.note.NoteAdapter
import com.dsvag.keepyournote.data.adapters.note.NoteDecoration
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
        viewModel.getNotes.observe(viewLifecycleOwner) { noteList: List<Note>? ->
            if (noteList != null) {
                noteAdapter.setData(noteList)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerview() {
        val noteDecoration = NoteDecoration(10)

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.recyclerview.addItemDecoration(noteDecoration)
        binding.recyclerview.adapter = noteAdapter

        val t = SwipeToDeleteCallback(noteAdapter)

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
}