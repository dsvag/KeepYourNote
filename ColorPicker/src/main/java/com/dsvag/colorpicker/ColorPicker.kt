package com.dsvag.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.dsvag.colorpicker.databinding.FragmentColorPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias ColorPickerListener = ((color: Int) -> Unit)

class ColorPicker : BottomSheetDialogFragment() {

    private var _binding: FragmentColorPickerBinding? = null
    private val binding get() = _binding!!

    private var adapter: ColorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColorPickerBinding.inflate(inflater, container, false)

        binding.closeButton.setOnClickListener { dismiss() }

        binding.recyclerview.adapter = adapter

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapter = null
    }

    fun colorPicker(colors: Array<Int>, listener: ColorPickerListener, span: Int = 6): ColorPicker {
        adapter = ColorAdapter(colors, listener)

        binding.recyclerview.layoutManager =
            GridLayoutManager(binding.root.context, span, GridLayoutManager.HORIZONTAL, false)

        return this
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, TAG)
    }

    companion object {
        private val TAG = ColorPicker::class.simpleName
    }
}