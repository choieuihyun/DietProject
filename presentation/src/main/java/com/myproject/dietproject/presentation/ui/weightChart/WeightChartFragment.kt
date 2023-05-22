package com.myproject.dietproject.presentation.ui.weightChart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.WeightChartFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import java.util.*


class WeightChartFragment: BaseFragment<WeightChartFragmentBinding>(R.layout.weight_chart_fragment)  {

//    private lateinit var lineChart: LineChart

    private lateinit var weightChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chartData = chartDataSetting()

        weightChart = binding.weightChart

        weightChart.data = chartData

        chartSetting(weightChart, chartData)

        chartMarkerSetting(weightChart)
    }


    // DataSet line custom
    private fun chartDataSetting() : LineData {

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

    }

    private fun chartSetting(lineChart: LineChart, lineData: LineData) {

        // 차트 범례 조정
        val legend: Legend = lineChart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER;
        legend.orientation = Legend.LegendOrientation.VERTICAL;
        legend.yEntrySpace = 10f;
        legend.textSize = 20f
        legend.xOffset = 80f;
        legend.yOffset = 20f;

        // x축 조정
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.enableGridDashedLine(8f, 24f, 0f)
        xAxis.spaceMin = 0.1f
        xAxis.spaceMax = 0.1f
        xAxis.textSize = 16f
        xAxis.setDrawGridLines(false)

        // y축(왼쪽) 조정
        val yLAxis: YAxis = lineChart.axisLeft
        yLAxis.textColor = Color.BLACK
        yLAxis.axisMinimum = 0f
        yLAxis.axisMaximum = lineData.yMax * 1.5.toFloat() // yMax하면 안됨 + 그냥 yMax로하면 차트 볼때 맨날 최대치 채워져있잖아 많이 먹은거마냥..
        yLAxis.axisLineWidth = 2f // 축의 굵기
        yLAxis.textSize = 16f
        yLAxis.setDrawGridLines(false)

        Log.d("yMax", lineData.yMax.toString())

        // y축(오른쪽) 조정
        val yRAxis: YAxis = lineChart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

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
}


