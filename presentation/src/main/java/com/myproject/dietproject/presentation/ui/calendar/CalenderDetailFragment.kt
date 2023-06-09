package com.myproject.dietproject.presentation.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.CalendarDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalenderDetailFragment : BaseFragment<CalendarDetailFragmentBinding>(R.layout.calendar_detail_fragment) {

    private val args by navArgs<CalenderDetailFragmentArgs>()
    private val viewModel: CalendarDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteDate.text = args.date

        binding.viewModel = viewModel

        viewModel.getCalendarDetailData(args.userId, args.date.toString()) // null 경우 처리

    }


}