package com.myproject.dietproject.presentation.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.CalendarDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.userkcal.KcalAdapter
import com.myproject.dietproject.presentation.ui.userkcal.KcalFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalenderDetailFragment : BaseFragment<CalendarDetailFragmentBinding>(R.layout.calendar_detail_fragment) {

    private lateinit var calendarDetailAdapter: CalendarDetailAdapter
    private val args by navArgs<CalenderDetailFragmentArgs>()
    private val viewModel: CalendarDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.noteDate.text = args.date

        binding.viewModel = viewModel

        viewModel.getCalendarDetailData(args.userId, args.date.toString()) // null 경우 처리

        viewModel.dayKcalList.observe(viewLifecycleOwner) {
            Log.d("calendarFragment", it.toString())
            calendarDetailAdapter.submitList(it)
        }


    }

    private fun setupRecyclerView() {

        calendarDetailAdapter = CalendarDetailAdapter()

        binding.calendarDetailRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = calendarDetailAdapter
        }

    }


}