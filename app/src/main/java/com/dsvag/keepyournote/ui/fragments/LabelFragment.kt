package com.dsvag.keepyournote.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsvag.keepyournote.data.adapters.ItemDecoration
import com.dsvag.keepyournote.data.adapters.label.LabelAdapter
import com.dsvag.keepyournote.data.adapters.label.LabelDiffUtilCallback
import com.dsvag.keepyournote.data.models.Label
import com.dsvag.keepyournote.data.viewmodels.LabelViewModel
import com.dsvag.keepyournote.databinding.FragmentLabelBinding

class LabelFragment : Fragment() {

    private var _binding: FragmentLabelBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, LabelViewModel.Factory(requireNotNull(this.activity).application))
            .get(LabelViewModel::class.java)
    }

    private val labelsAdapter by lazy { LabelAdapter(::onClick) }

    private var label = Label(title = "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLabelBinding.inflate(inflater, container, false)

        initRv()

        binding.add.setOnClickListener {
            val text = binding.newLabel.text.toString().trim()

            if (text.isNotEmpty()) {
                viewModel.insertLabel(Label(title = text))
                binding.newLabel.setText("")
            }
        }

        binding.clear.setOnClickListener {
            viewModel.removeLabel(label)
            binding.newLabel.setText("")
        }

        binding.newLabel.addTextChangedListener {
            val text = it.toString()
            label = label.copy(title = text)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getLabels.observe(viewLifecycleOwner) { newLabelList ->
            if (newLabelList != null) {
                labelsAdapter.setData(
                    newLabelList, DiffUtil.calculateDiff(
                        LabelDiffUtilCallback(labelsAdapter.getData(), newLabelList)
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRv() {
        val itemDecoration = ItemDecoration(10)

        binding.labels.layoutManager = LinearLayoutManager(requireContext())
        binding.labels.setHasFixedSize(true)
        binding.labels.addItemDecoration(itemDecoration)
        binding.labels.adapter = labelsAdapter
    }

    private fun onClick(label: Label) {
        this.label = label
        binding.newLabel.setText(label.title)
        binding.newLabel.setSelection(label.title.length)
        viewModel.removeLabel(label)
    }
}