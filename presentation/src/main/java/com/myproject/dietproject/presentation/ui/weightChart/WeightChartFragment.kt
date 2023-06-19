package com.myproject.dietproject.presentation.ui.weightChart

import android.graphics.Color
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.WeightChartFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat


@AndroidEntryPoint
class WeightChartFragment: BaseFragment<WeightChartFragmentBinding>(R.layout.weight_chart_fragment) {

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

        /*        viewModel.weekDateArray.observe(viewLifecycleOwner) { weekDateArray ->
            viewModel.weekKcalArray.observe(viewLifecycleOwner) { weekKcalArray ->

                viewModel.updateChartData()

                setupChartUI(viewModel.entries).let {

                        chartData -> weightChart.data = chartData
                                     chartSetting(weightChart, chartData)
                                     chartMarkerSetting(weightChart)

                }
            }
        }*/

        viewModel.weekDateArray.observe(viewLifecycleOwner) { weekData ->
            viewModel.weekKcalArray.observe(viewLifecycleOwner) {

                viewModel.updateChartData()

                setupChartData(viewModel.entries).let {

                        chartData ->
                    weightChart.data = chartData
                    setupChartUI(weightChart, chartData)
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

    private fun onPreviousButtonClicked(userId: String) {

        viewModel.resetPreviousWeekData()

        val currentStartOfWeek = viewModel.startOfWeek.value
        val currentEndOfWeek = viewModel.endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        dateFormat.parse(currentStartOfWeek!!)
        dateFormat.parse(currentEndOfWeek!!)

        viewModel.getPreviousWeekData(userId, currentStartOfWeek, currentEndOfWeek)

        Log.d("chartStartOfPreviousWeekFragment", currentStartOfWeek.toString())
        Log.d("chartEndOfPreviousWeekFragment", currentEndOfWeek.toString())

        viewModel.previousWeekDateArray.observe(viewLifecycleOwner) { weekEvent ->
            weekEvent.getContentIfNotHandled()?.let { weekData ->
                viewModel.previousWeekKcalArray.observe(viewLifecycleOwner) { kcalEvent ->
                    kcalEvent.getContentIfNotHandled()?.let {

                        viewModel.updateChartPreviousData() // 왜 viewModel의 get function에서 안쓰냐면 위의 데이터들이 변화되고 난 후 변화된 데이터들로 이루어진 entries를 갈아줘야 제대로 된 엔트리를 사용하지
                        // 실제로 viewModel.getPreviousWeekData(userId, currentStartOfWeek, currentEndOfWeek) 여기 아래에 array 로그 찍어보면 뭔지 알 수 있음.
                        setupChartData(viewModel.entries).let {

                                chartData ->
                            weightChart.data = chartData
                            setupChartUI(weightChart, chartData)
                            chartMarkerSetting(weightChart)

                            Log.d(
                                "previousDateArrayFragment",
                                viewModel.previousWeekDateArray.value?.peekContent().toString()
                            )
                            Log.d(
                                "previousKcalArrayFragment",
                                viewModel.previousWeekKcalArray.value?.peekContent().toString()
                            )
                            Log.d("previousEntryFragment", viewModel.entries.toString())
                            Log.d("previousLineDataFragment", chartData.dataSets.toString())

                        }
                    }
                }
            }
        }
    }

    // DataSet line custom
    private fun setupChartData(data: List<Entry>): LineData {

        val lineDataSet = LineDataSet(data, "Kcal")
        // 아 아까 데이터 여러개 붙였을때도 안됐던 이유가 x를 마구잡이로해서(역순) 그랬던 것이구나. x는 오름차순이 되어야함.
        // 그렇다고 정렬할 수는 없다? 는 아닌거같은데 흠 x기준으로 정렬하는게 맞지 사실. 근데 애초에 날짜 순으로 입력되는데 굳이 정렬을?
        val lineData = LineData()
        lineData.addDataSet(lineDataSet)

        Log.d("lineData", lineData.dataSets.toString())
        Log.d("lineDataSet", lineDataSet.values.toString())

        lineDataSet.apply {
            Log.d("dssf",values.toString())
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

    private fun setupChartUI(lineChart: LineChart, lineData: LineData) {

        // 차트 범례 조정
        val legend: Legend = lineChart.legend
        legend.apply {
            form = Legend.LegendForm.CIRCLE
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.VERTICAL
            yEntrySpace = 10f
            textSize = 20f
            xOffset = 80f
            yOffset = 20f
        }

        val xVals = ArrayList<String>() // 변환할 String 형태 x축 값
        for (i in 0..4) {
            val xValue = (i + 1).toString() + "번째"
            Log.d("x값 확인", xValue)
            xVals.add(xValue)
        }


        // x축 조정
        val xAxis: XAxis = lineChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return viewModel.previousWeekDateArray.value?.peekContent()?.get(value.toInt()) ?: ""
                }
            }
            enableGridDashedLine(8f, 24f, 0f)
            spaceMin = 0.1f
            spaceMax = 0.1f
            textSize = 16f
            setDrawGridLines(false)
        }


        Log.d("sdfsdf", xVals.toString())

        // y축(왼쪽) 조정
        val yLAxis: YAxis = lineChart.axisLeft
        yLAxis.apply {
            textColor = Color.BLACK
            axisMinimum = 0f

            if(lineData.yMax > 0)
                axisMaximum = lineData.yMax * 1.2F// yMax하면 안됨 + 그냥 yMax로하면 차트 볼때 맨날 최대치 채워져있잖아 많이 먹은거마냥..

            axisLineWidth = 2f // 축의 굵기
            textSize = 16f
            setDrawGridLines(false)
        }

        Log.d("linedata",lineData.dataSets.toString())

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

        // XAxis에 원하는 String 설정하기 (날짜)
//        xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return LABEL.get(range).get(value.toInt())
//            }
//        }

        lineChart.invalidate()
    }

    private fun chartMarkerSetting(lineChart: LineChart) {
        val marker =
            ChartMarkerView(requireContext(), R.layout.weight_chart_marker) // 이것도 구조 변경해야함.
        marker.chartView = lineChart
        lineChart.marker = marker
    }



}


