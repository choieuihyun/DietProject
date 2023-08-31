package com.myproject.dietproject.presentation.ui.weightChart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.room.Index
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.WeightChartFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class WeightChartFragment :
    BaseFragment<WeightChartFragmentBinding>(R.layout.weight_chart_fragment) {

//    private lateinit var lineChart: LineChart

    private lateinit var weightChart: BarChart
    private lateinit var auth: FirebaseAuth
    private lateinit var mainActivity: MainActivity

    private val viewModel: WeightChartViewModel by viewModels()

    var entries = ArrayList<Entry>() // 값 - 인덱스 넣어주면 순차적으로 그려줘, y축이름(데이터값)

    var xVals = ArrayList<String>() // X 축 이름 값

    val dayArray = mutableListOf<String>()


    fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    val rawData = listOf(
        Pair("2023-07-27", 502f),
        Pair("2023-07-28", 332f),
        Pair("2023-07-29", 3411f),
        Pair("2023-07-30", 460f),
        Pair("2023-07-31", 900f),
        Pair("2023-08-01", 160f),
        Pair("2023-08-02", 200f)
    )

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

        binding.weightChartViewModel = viewModel

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
                    Log.d("sdfsssWeek", viewModel.weekDateArray.value?.peekContent().toString())
                    weightChart.data = chartData
                    setupChartUI(weightChart, weightChart.data, viewModel.entries)
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

                        viewModel.updateChartPreviousData() // 왜 viewModel의 get function에서 안쓰냐면 위의 데이터들이 변화되고 난 후 변화된 데이터들로 이루어진 entries를 갈아줘야 제대로 된 엔트리를 사용하지
                        // 실제로 viewModel.getPreviousWeekData(userId, currentStartOfWeek, currentEndOfWeek) 여기 아래에 array 로그 찍어보면 뭔지 알 수 있음.
                        setupChartData(viewModel.entries).let {

                                chartData ->
                            weightChart.data = chartData
                            setupChartUI(weightChart, weightChart.data, viewModel.entries)
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
                            setupChartUI(weightChart, weightChart.data, viewModel.entries)
                            chartMarkerSetting(weightChart)

                        }
                    }
                }
            }
        }
    }

    // DataSet line custom
    // DataSet line custom
    private fun setupChartData(data: List<BarEntry>): BarData {

        xVals.clear()

        val barDataSet = BarDataSet(data, "Kcal")

        /*        for(i in data.indices) {
                    xVals.add(data[i].x.toString())
                }*/

        Log.d("bardata", barDataSet.values[0].toString())

        for (i in data.indices) {
            xVals.add(data[i].x.toString())
        }

        Log.d("sdfsdf222222", xVals.toString())

        // 아 아까 데이터 여러개 붙였을때도 안됐던 이유가 x를 마구잡이로해서(역순) 그랬던 것이구나. x는 오름차순이 되어야함.
        // 그렇다고 정렬할 수는 없다? 는 아닌거같은데 흠 x기준으로 정렬하는게 맞지 사실. 근데 애초에 날짜 순으로 입력되는데 굳이 정렬을?
        val barData = BarData()
        barData.addDataSet(barDataSet)

        barDataSet.apply {
            color = Color.WHITE

            setDrawValues(false)
        }

        return barData

    }

    private fun setupChartUI(barChart: BarChart, barData: BarData, data: MutableList<BarEntry>) {

        // 차트 범례 조정
        val legend: Legend = barChart.legend
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

        //        for(i in 0..6) {
//            val xArray = lineData.
//        }

        // x축 조정
        val xAxis: XAxis = barChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            enableGridDashedLine(8f, 24f, 0f)
            spaceMin = 0.1f
            spaceMax = 0.1f
            textSize = 16f
            textColor = Color.WHITE


            /*            for (i in data.indices) {
                            val days = data[i].x.toInt().toString()
                            dayArray.add(days)
                            Log.d("sibal", days)
                        }*/


/*            valueFormatter = object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {

                    return value.toString()
                }
            }*/
            setDrawGridLines(false)
        }

        // y축(왼쪽) 조정
        val yLAxis: YAxis = barChart.axisLeft
        yLAxis.apply {
            textColor = Color.BLACK
            axisMinimum = 0f

            if (barData.yMax > 0)
                axisMaximum =
                    barData.yMax * 1.2F// yMax하면 안됨 + 그냥 yMax로하면 차트 볼때 맨날 최대치 채워져있잖아 많이 먹은거마냥..

            axisLineWidth = 2f // 축의 굵기
            textSize = 16f
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        // y축(오른쪽) 조정
        val yRAxis: YAxis = barChart.axisRight
        yRAxis.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        val description = Description()
        description.text = ""
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.description = description
        barChart.animateY(2000, Easing.EaseInCubic)

        barChart.invalidate()
    }

    private fun chartMarkerSetting(barChart: BarChart) {
        val marker =
            ChartMarkerView(requireContext(), R.layout.weight_chart_marker) // 이것도 구조 변경해야함.
        marker.chartView = barChart
        barChart.marker = marker

    }


}


