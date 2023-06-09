package com.myproject.dietproject.presentation.ui.calendar


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.CalendarDayLayoutBinding
import com.myproject.dietproject.presentation.databinding.CalendarFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class CalendarFragment: BaseFragment<CalendarFragmentBinding>(R.layout.calendar_fragment) {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCalendarView()
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        var date = CalendarDayLayoutBinding.bind(view).calendarDayText
        //var today = CalendarDayLayoutBinding.bind(view).ivTodayDot
    }

    private fun setCalendarView() {
        with(binding.calendarView) {
            itemAnimator = null
            doOnPreDraw {
                daySize = Size(binding.calendarView.width/7, binding.calendarView.height/5) // cell의 height
            }

            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(240)
            val lastMonth = currentMonth.plusMonths(240)
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            setup(firstMonth, lastMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)

            dayBinder = object : DayBinder<DayViewContainer> {

                override fun create(view: View): DayViewContainer {
                    return DayViewContainer(view)
                }

                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    val intent = Intent(requireContext(), com.myproject.dietproject.presentation.ui.MainActivity::class.java)
                    container.view.setOnClickListener {
                        val clickedDate = "${day.date}" // df.format(day.date.monthValue) 인데 에러때문에 빼봄
                        val action = CalendarFragmentDirections.actionCalendarFragmentToCalenderDetailFragment(auth.currentUser!!.uid, clickedDate)
                        findNavController().navigate(action)
                        Log.d("sdfsdf", clickedDate.toString())
                    }


                    container.date.text = day.date.dayOfMonth.toString()

                    if(day.owner  != DayOwner.THIS_MONTH) {

                        container.date.setTextColor(ContextCompat.getColor(context, R.color.amber_500))

                        if(day.date.isAfter(LocalDate.now())) {
                            container.date.setTextColor(ContextCompat.getColor(context, R.color.indigo_500))
                        }

                    } else {

                        when(day.date) {
                            LocalDate.now() -> {

                                container.date.typeface = resources.getFont(R.font.gmarket_ttf_medium)
                                container.date.setTextColor(Color.BLACK)
                                //container.today.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }

            binding.btnMonthNext.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.next)

                }
            }

            binding.btnMonthPrev.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                }
            }

            binding.tvCalendarIndicator.text = currentMonth.toString() // 해당 년-월 (2023-05) 달력 위에 띄워주는 부분인데 이거 LiveData로 바꿔서 ViewModel에서 계속 갱신하면서 받아오도록하면 될듯?
            // 이거 지금 못하는 이유가 이걸 저기 button에 넣어버리면 계속 뷰가 갱신되는데 그거보단 그냥 viewModel에서 데이터 변경해서 받아와서 LiveData로 바꿔주는게 맞다 이건.
        }
    }

//    private fun onMonthScroll(currentMonth: YearMonth) {
//        val visibleMonth = binding.calendarView.findFirstVisibleMonth() ?: return
//        if(currentMonth != visibleMonth.yearMonth) {
//            binding.calendarView.smoothScrollToMonth(currentMonth)
//        }
//    }
}