package com.myproject.dietproject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.myproject.dietproject.R


@SuppressLint("ViewConstructor") // 생성자 매개변수 뭔가 바꿔야 할것같은 느낌(layoutResource) 전체적으로 바꿀게 많은듯?
class ChartMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContentHead)

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        if (e is CandleEntry) {
            val ce = e as CandleEntry
            tvContent.text = "" + Utils.formatNumber(ce.high, 0, true) + " Kcal"
        } else {
            tvContent.text = "" + Utils.formatNumber(e.y, 0, true) + " Kcal"
        }
        super.refreshContent(e, highlight)
    }

    // 마커 위치 조정 메소드
    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height * 1.5).toFloat())
    }


}