package com.myproject.dietproject.presentation.ui.weightChart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.WeightChartFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import com.myproject.dietproject.presentation.ui.util.BackPressedHandler
import com.myproject.dietproject.presentation.ui.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat


@AndroidEntryPoint
class WeightChartFragment :
    BaseFragment<WeightChartFragmentBinding>(R.layout.weight_chart_fragment) {

//    private lateinit var lineChart: LineChart

    private lateinit var weightChart: LineChart
    private lateinit var auth: FirebaseAuth
    private lateinit var mainActivity: MainActivity

    private val viewModel: WeightChartViewModel by viewModels()

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

        binding.weightChartViewModel = viewModel

        viewModel.getUserWeight(auth.currentUser!!.uid)
        viewModel.getChartWeekData(auth.currentUser!!.uid)

        weightChart = binding.weightChart

        viewModel.weekDateArray.observe(viewLifecycleOwner) { weekData ->
            viewModel.weekKcalArray.observe(viewLifecycleOwner) {

                viewModel.updateChartData()

                setupChartData(viewModel.entries).let {

                        chartData ->
                    weightChart.data = chartData
                    setupChartUI(weightChart, weightChart.data,viewModel.weekDateArray.value!!.peekContent())
                    chartMarkerSetting(weightChart)

                }
            }
        }

        viewModel.isNextButtonEnabled.observe(viewLifecycleOwner) {
            binding.chartBtnWeekNext.isEnabled = it
        }

        binding.chartBtnWeekNext.setOnClickListener {
            viewModel.moveNextWeek()
            onNextButtonClicked(auth.currentUser!!.uid)
        }

        binding.chartBtnWeekPrev.setOnClickListener {
            viewModel.movePreviousWeek()
            onPreviousButtonClicked(auth.currentUser!!.uid)
        }

    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    private fun onPreviousButtonClicked(userId: String) {

        val currentStartOfWeek = viewModel.startOfWeek.value
        val currentEndOfWeek = viewModel.endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        dateFormat.parse(currentStartOfWeek!!)
        dateFormat.parse(currentEndOfWeek!!)

        viewModel.getPreviousWeekData(userId, currentStartOfWeek, currentEndOfWeek)

        viewModel.previousWeekDateArray.observe(viewLifecycleOwner) { weekEvent ->
            weekEvent.getContentIfNotHandled()?.let { weekData ->
                viewModel.previousWeekKcalArray.observe(viewLifecycleOwner) { kcalEvent ->
                    kcalEvent.getContentIfNotHandled()?.let {

                        viewModel.updateChartPreviousData()

                        setupChartData(viewModel.entries).let {

                                chartData ->
                            weightChart.data = chartData
                            setupChartUI(weightChart, weightChart.data,
                                viewModel.previousWeekDateArray.value!!.peekContent())
                            chartMarkerSetting(weightChart)
                        }
                    }
                }
            }
        }
    }

    private fun onNextButtonClicked(userId: String) {

        val currentStartOfWeek = viewModel.startOfWeek.value
        val currentEndOfWeek = viewModel.endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        dateFormat.parse(currentStartOfWeek!!)
        dateFormat.parse(currentEndOfWeek!!)

        viewModel.getNextWeekData(userId, currentStartOfWeek, currentEndOfWeek)

        viewModel.nextWeekDateArray.observe(viewLifecycleOwner) { weekEvent ->
            weekEvent.getContentIfNotHandled()?.let { weekData ->
                viewModel.nextWeekKcalArray.observe(viewLifecycleOwner) { kcalEvent ->
                    kcalEvent.getContentIfNotHandled()?.let {

                        viewModel.updateChartNextData()

                        setupChartData(viewModel.entries).let {

                                chartData ->
                            weightChart.data = chartData
                            setupChartUI(weightChart, weightChart.data,viewModel.nextWeekDateArray.value!!.peekContent())
                            chartMarkerSetting(weightChart)

                        }
                    }
                }
            }
        }
    }

    private fun setupChartData(data: MutableList<Entry>): LineData {

        val lineDataSet = LineDataSet(data, "Kcal")

        val lineData = LineData()
        lineData.addDataSet(lineDataSet)

        lineDataSet.apply {
            color = Color.WHITE

            setDrawValues(false)
        }

        return lineData

    }

    private fun setupChartUI(lineChart: LineChart, lineData: LineData, xValsArray: MutableList<String>) {

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

        // x축 조정
        val xAxis: XAxis = lineChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            enableGridDashedLine(8f, 24f, 0f)
            spaceMin = 0.1f
            spaceMax = 0.1f
            textSize = 16f
            textColor = Color.WHITE
            valueFormatter = MyValueFormatter(xValsArray)

            setDrawGridLines(false)
        }

        // y축(왼쪽) 조정
        val yLAxis: YAxis = lineChart.axisLeft
        yLAxis.apply {
            textColor = Color.BLACK
            axisMinimum = 0f

            if(lineData.yMax > 0)
                axisMaximum = lineData.yMax * 1.2F// yMax하면 안됨 + 그냥 yMax로하면 차트 볼때 맨날 최대치 채워져있잖아 많이 먹은거마냥..

            axisLineWidth = 2f // 축의 굵기
            textSize = 16f
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

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

        lineChart.invalidate()
    }

    private fun chartMarkerSetting(lineChart: LineChart) {
        val marker =
            ChartMarkerView(requireContext(), R.layout.weight_chart_marker) // 이것도 구조 변경해야함.
        marker.chartView = lineChart
        lineChart.marker = marker

    }

    class MyValueFormatter(private val xValues: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index >= 0 && index < xValues.size) {
                xValues[index]
            } else {
                ""
            }
        }
    }

}



