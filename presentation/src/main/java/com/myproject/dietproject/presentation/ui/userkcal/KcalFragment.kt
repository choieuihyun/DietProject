package com.myproject.dietproject.presentation.ui.userkcal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalFragment : BaseFragment<KcalFragmentBinding>(R.layout.kcal_fragment) {

    private lateinit var kcalListAdapter : KcalAdapter
    private val viewModel: KcalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupEdittext()

        setupUI()

    }

    private fun setupUI() {

        binding.searchButton.setOnClickListener {

            val searchText = binding.appCompatEditText.text.toString()

            viewModel.getKcalData(searchText)

            viewModel.kcalData.observe(viewLifecycleOwner) {
                if (it != null) {
                    kcalListAdapter.submitList(it)
                } else {
                    binding.addUserKcalRecyclerView.visibility = View.GONE
                }
            }
        }

    }

    private fun setupRecyclerView() {

        kcalListAdapter = KcalAdapter()

        binding.addUserKcalRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = kcalListAdapter
        }

        kcalListAdapter.setOnItemClickListener {
            Log.d("AdapterClickEvent", it.toString())
        }

    }

    private fun setupEdittext() {

        binding.appCompatEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }


}