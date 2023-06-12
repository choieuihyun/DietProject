package com.myproject.dietproject.presentation.ui.weightChart

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.WeightChartFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WeightChartFragment: BaseFragment<WeightChartFragmentBinding>(R.layout.weight_chart_fragment)  {

//    private lateinit var lineChart: LineChart

    private lateinit var weightChart: LineChart
    private lateinit var auth: FirebaseAuth

    private val viewModel: WeightChartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChartWeekData(auth.currentUser!!.uid)

        weightChart = binding.weightChart

        viewModel.weekDateArray.observe(viewLifecycleOwner) {
                weekDateArray -> viewModel.weekKcalArray.observe(viewLifecycleOwner) {

                    weekKcalArray -> setupChartUI(viewModel.setChartData()).let {

                        chartData -> weightChart.data = chartData
                                     chartSetting(weightChart, chartData)
                                     chartMarkerSetting(weightChart)

                }
            }
        }


        viewModel.startOfWeek.observe(viewLifecycleOwner) {
            binding.startOfWeek.text = it
        }

        viewModel.endOfWeek.observe(viewLifecycleOwner) {
            binding.endOfWeek.text = it
        }

        viewModel.isNextButtonEnabled.observe(viewLifecycleOwner) {
            binding.chartBtnWeekNext.isEnabled = it
        }

        binding.chartBtnWeekNext.setOnClickListener {
            viewModel.moveNextWeek()
        }

        binding.chartBtnWeekPrev.setOnClickListener {
            viewModel.movePreviousWeek()
            onPreviousButtonClicked(auth.currentUser!!.uid)
        }

    }

    // DataSet line custom
    private fun setupChartUI(data: List<Entry>) : LineData {

        // 아 아까 데이터 여러개 붙였을때도 안됐던 이유가 x를 마구잡이로해서(역순) 그랬던 것이구나. x는 오름차순이 되어야함.
        // 그렇다고 정렬할 수는 없다? 는 아닌거같은데 흠 x기준으로 정렬하는게 맞지 사실. 근데 애초에 날짜 순으로 입력되는데 굳이 정렬을?
        val lineData = LineData()
        val lineDataSet = LineDataSet(data, "Kcal")

        lineData.addDataSet(lineDataSet)

        lineDataSet.apply {
            lineWidth = 2f
            circleRadius = 6f
            setCircleColor(Color.parseColor("#FFA1B4DC"))
            color = Color.parseColor("#FFA1B4DC")
            setDrawCircleHole(true)
            setDrawCircles(true)
            setDrawHorizontalHighlightIndicator(false)
            setDrawHighlightIndicators(false)
            setDrawValues(false)

        }

        return lineData

    }

/*    private fun chartDataSetting() : LineData {

        val lineData = LineData()

        val entries: ArrayList<Entry> = ArrayList() // 여긴 나중에 데이터 받아올테니까 getChartData 메소드로 빼줘야하나?
        entries.add(Entry(0.0F,5.0F)) // 아마 x가 날짜, y가 칼로리가 될 듯 하다.
        entries.add(Entry(1.0F,2.0F))
        entries.add(Entry(2.0F,3.0F))
        entries.add(Entry(3.0F,1.0F))
        entries.add(Entry(4.0F,4.0F))
        entries.add(Entry(5.0F,6.0F))
        entries.add(Entry(6.0F,3.0F))


        // 아 아까 데이터 여러개 붙였을때도 안됐던 이유가 x를 마구잡이로해서(역순) 그랬던 것이구나. x는 오름차순이 되어야함.
        // 그렇다고 정렬할 수는 없다? 는 아닌거같은데 흠 x기준으로 정렬하는게 맞지 사실. 근데 애초에 날짜 순으로 입력되는데 굳이 정렬을?

        val lineDataSet = LineDataSet(entries, "Kcal")
        lineData.addDataSet(lineDataSet)

        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 6f
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"))
        lineDataSet.circleHoleColor // (Color.BLUE)가 안들어가는데?
        lineDataSet.color = Color.parseColor("#FFA1B4DC")
        lineDataSet.setDrawCircleHole(true)
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawValues(false)

        return lineData

    }*/

    private fun chartSetting(lineChart: LineChart, lineData: LineData) {

        // 차트 범례 조정
        val legend: Legend = lineChart.legend
        legend.apply {
            form = Legend.LegendForm.CIRCLE
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER;
            orientation = Legend.LegendOrientation.VERTICAL;
            yEntrySpace = 10f;
            textSize = 20f
            xOffset = 80f;
            yOffset = 20f;
        }

        // x축 조정
        val xAxis: XAxis = lineChart.xAxis
        xAxis.apply{
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            enableGridDashedLine(8f, 24f, 0f)
            spaceMin = 0.1f
            spaceMax = 0.1f
            textSize = 16f
            setDrawGridLines(false)
        }

        // y축(왼쪽) 조정
        val yLAxis: YAxis = lineChart.axisLeft
        yLAxis.apply {
            textColor = Color.BLACK
            axisMinimum = 0f
            axisMaximum = lineData.yMax * 1.5.toFloat() // yMax하면 안됨 + 그냥 yMax로하면 차트 볼때 맨날 최대치 채워져있잖아 많이 먹은거마냥..
            axisLineWidth = 2f // 축의 굵기
            textSize = 16f
            setDrawGridLines(false)
        }

        Log.d("yMax", lineData.yMax.toString())

        // y축(오른쪽) 조정
        val yRAxis: YAxis = lineChart.axisRight
        yRAxis.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        val description = Description()
        description.text = ""

        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.description = description
        lineChart.animateY(2000, Easing.EaseInCubic)
        // EasingOption.EaseInCubic 원래 이거였음음
        lineChart.invalidate()

//        // XAxis에 원하는 String 설정하기 (날짜)
//        xAxis.setValueFormatter(object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return LABEL.get(range).get(value.toInt())
//            }
//        })
    }

    private fun chartMarkerSetting(lineChart: LineChart) {
        val marker = ChartMarkerView(requireContext(), R.layout.weight_chart_marker) // 이것도 구조 변경해야함.
        marker.chartView = lineChart
        lineChart.marker = marker
    }


    private fun onPreviousButtonClicked(userId: String) {

        viewModel.resetPreviousWeekData()

        val currentStartOfWeek = viewModel.startOfWeek.value
        val currentEndOfWeek = viewModel.endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()

        calendar.time = dateFormat.parse(currentEndOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, -7)
        val previousEndOfWeek = dateFormat.format(calendar.time)

        calendar.time = dateFormat.parse(previousEndOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, -6)
        val previousStartOfWeek = dateFormat.format(calendar.time)

//        calendar.time = dateFormat.parse(currentStartOfWeek.toString())
//        calendar.add(Calendar.DAY_OF_WEEK, -7)
//        val previousStartOfWeek = dateFormat.format(calendar.time)
//
//        calendar.time = dateFormat.parse(currentEndOfWeek.toString())
//        calendar.add(Calendar.DAY_OF_WEEK, -7)
//        val previousEndOfWeek = dateFormat.format(calendar.time)

        viewModel.getPreviousWeekData(userId, previousStartOfWeek, previousEndOfWeek)
    }
}


