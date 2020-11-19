package com.dsvag.keepyournote.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsvag.keepyournote.data.adapters.ItemDecoration
import com.dsvag.keepyournote.data.adapters.label.LabelDiffUtilCallback
import com.dsvag.keepyournote.data.adapters.label.LabelPickAdapter
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

    private val labelsAdapter by lazy { LabelPickAdapter(::onClickListener) }

    private var label = Label(title = "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLabelBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        initRv()

        binding.add.setOnClickListener {
            val text = binding.newLabel.text.toString().trim()

            if (text.isNotEmpty()) {
                viewModel.insertLabel(Label(title = text))
                binding.newLabel.text?.clear()
                binding.newLabel.clearFocus()
            }
        }

        binding.clear.setOnClickListener {
            viewModel.deleteLabel(label)
            binding.newLabel.text?.clear()
        }

        binding.newLabel.addTextChangedListener {
            label.title = it.toString().trim()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRv() {
        binding.labels.layoutManager = LinearLayoutManager(requireContext())
        binding.labels.setHasFixedSize(true)
        binding.labels.addItemDecoration(ItemDecoration(10))
        binding.labels.adapter = labelsAdapter
    }

    private fun onClickListener(label: Label) {
        this.label = label
        binding.newLabel.setText(label.title)
        binding.newLabel.setSelection(label.title.length)
        binding.newLabel.requestFocus()
    }
}