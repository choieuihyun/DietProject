package com.myproject.dietproject.presentation.ui.calendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.CalendarDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalenderDetailFragment : BaseFragment<CalendarDetailFragmentBinding>(R.layout.calendar_detail_fragment) {

    private lateinit var calendarDetailAdapter: CalendarDetailAdapter
    private val args by navArgs<CalenderDetailFragmentArgs>()
    private lateinit var mainActivity: MainActivity
    private val viewModel: CalendarDetailViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.noteDate.text = args.date

        binding.viewModel = viewModel

        viewModel.getCalendarDetailData(args.userId, args.date.toString()) // null 경우 처리
        viewModel.getRecommendKcalData(args.userId)

        viewModel.dayKcalList.observe(viewLifecycleOwner) {
            calendarDetailAdapter.submitList(it)
        }

        viewModel.imageResultLiveData.observe(viewLifecycleOwner) {
            imageViewSetting()
        }



    }

    override fun onPause() {
        super.onPause()

    }

    private fun setupRecyclerView() {

        calendarDetailAdapter = CalendarDetailAdapter()

        binding.calendarDetailRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = calendarDetailAdapter
        }

    }

    private fun imageViewSetting() {

        if (viewModel.imageResultLiveData.value == 1)

            binding.calendarDetailImageView.setImageResource(R.drawable.calendar_detail_weight)

        else

            binding.calendarDetailImageView.setImageResource(R.drawable.calendar_detail_good)

    }


}