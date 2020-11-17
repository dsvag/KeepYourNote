package com.dsvag.keepyournote.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.adapters.ItemDecoration
import com.dsvag.keepyournote.data.adapters.color.ColorAdapter
import com.dsvag.keepyournote.databinding.SheetColorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ColorSheet : BottomSheetDialogFragment() {

    private var _binding: SheetColorBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ColorAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = SheetColorBinding.inflate(inflater, container, false)

        binding.colors.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.colors.setHasFixedSize(true)
        binding.colors.adapter = adapter
        binding.colors.addItemDecoration(ItemDecoration(24))

        adapter.setData(this, getColors())

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getColors() = intArrayOf(
        0,
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

    fun setOnClickListener(onClick: (Int) -> Unit): ColorSheet {
        adapter.setOnClickListener(onClick)
        return (this)
    }
}