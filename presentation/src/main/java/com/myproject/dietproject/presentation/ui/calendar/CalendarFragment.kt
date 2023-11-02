package com.myproject.dietproject.presentation.ui.calendar


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.myproject.dietproject.presentation.ui.MainActivity
import com.myproject.dietproject.presentation.ui.util.BackPressedHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


@AndroidEntryPoint
class CalendarFragment : BaseFragment<CalendarFragmentBinding>(R.layout.calendar_fragment) {

    private lateinit var auth: FirebaseAuth

    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BackPressedHandler.handleBackPress(this)

        viewModel.getCalendarDate()

        setCalendarView()

    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        var date = CalendarDayLayoutBinding.bind(view).calendarDayText
    }

    private fun setCalendarView() {

        binding.calendarView.apply {

            itemAnimator = null

            doOnPreDraw {
                daySize = Size(
                    binding.calendarView.width / 7,
                    binding.calendarView.height / 5
                ) // cell의 width, height
            }

            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(120)
            val lastMonth = currentMonth.plusMonths(120)

            setup(firstMonth, lastMonth, DayOfWeek.MONDAY) // 여기에 넣으면 일요일 시작
            scrollToMonth(currentMonth)

            dayBinder = object : DayBinder<DayViewContainer> {

                override fun create(view: View): DayViewContainer {
                    return DayViewContainer(view)
                }

                override fun bind(container: DayViewContainer, day: CalendarDay) {

                    val dayOfWeek = day.date.dayOfWeek

                    container.view.setOnClickListener {
                        val clickedDate =
                            "${day.date}" // df.format(day.date.monthValue) 인데 에러때문에 빼봄

                        if (day.owner == DayOwner.THIS_MONTH) {
                            val action =
                                CalendarFragmentDirections.actionCalendarFragmentToCalenderDetailFragment(
                                    auth.currentUser!!.uid,
                                    clickedDate
                                )
                            findNavController().navigate(action)
                        }
                    }


                    container.date.text = day.date.dayOfMonth.toString()

                    /*                    container.date.isVisible = day.owner == DayOwner.THIS_MONTH

                                        when(day.date) {
                                            LocalDate.now() -> {
                                                container.date.typeface = resources.getFont(R.font.gmarket_ttf_medium)
                                                container.date.setTextColor(Color.BLACK)
                                            }
                                        }*/

                    if (day.owner != DayOwner.THIS_MONTH) { // 해당 달이 아닌 날짜

                        if (dayOfWeek == DayOfWeek.SUNDAY) { // 일요일

                            container.date.typeface = resources.getFont(R.font.kangwon_light)

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.invisible20red
                                )
                            )

                        } else if (dayOfWeek == DayOfWeek.SATURDAY) { // 토요일

                            container.date.typeface = resources.getFont(R.font.kangwon_light)

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.invisible20blue
                                )
                            )

                        } else { // 월 화 수 목 금요일

                            container.date.typeface = resources.getFont(R.font.kangwon_light)

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.invisible20
                                )
                            )

                        }
                    }

                    if (day.owner == DayOwner.THIS_MONTH) { // 해당 달에 포함 되는 날짜

                        if (dayOfWeek == DayOfWeek.SUNDAY) { // 일요일

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red_700
                                )
                            )

                        } else if (dayOfWeek == DayOfWeek.SATURDAY) { // 토요일

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.blue_700
                                )
                            )
                        } else {

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.black
                                )
                            )

                        }
                        if (day.date == LocalDate.now()) { // 현재 날짜
                            container.date.typeface =
                                resources.getFont(R.font.kangwon_light)

                            container.date.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )

                            container.date.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.calendar_day_background_today
                            )

                  } else {
                            container.date.typeface = resources.getFont(R.font.kangwon_light)
                            container.date.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.calendar_day_background
                            )
                        }
                    }


                }

            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        binding.tvCalendarIndicator.text =
                            findFirstVisibleMonth()?.yearMonth.toString() // 슬라이드로 넘겼을 때 해당 달의 텍스트 변경

                    }
                }
            })


            binding.btnMonthNext.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                    binding.tvCalendarIndicator.text = it.yearMonth.next.toString()
                }
            }

            binding.btnMonthPrev.setOnClickListener {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                    binding.tvCalendarIndicator.text = it.yearMonth.previous.toString()
                }
            }

            binding.tvCalendarIndicator.text = currentMonth.toString()
        }

    }

}