package com.myproject.dietproject.presentation.ui.userkcal

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.LoadingProgress
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalFragment : BaseFragment<KcalFragmentBinding>(R.layout.kcal_fragment) {

    private lateinit var kcalListAdapter : KcalAdapter
    private val viewModel: KcalViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var dialog: Dialog
    private val args by navArgs<KcalFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = false

        dialog = LoadingProgress(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupEdittext()

        setupUI()

    }

    override fun onPause() {
        super.onPause()

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

            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) dialog.show()
                else dialog.dismiss()
            }
        }

    }

    private fun setupRecyclerView() {

        kcalListAdapter = KcalAdapter()

        binding.addUserKcalRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = kcalListAdapter
        }

        kcalListAdapter.setOnItemClickListener {
            kcal ->
            val action = KcalFragmentDirections.actionKcalFragmentToKcalDetailFragment(args.userId, kcal)
            findNavController().navigate(action)
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