package com.myproject.dietproject.presentation.ui.weightChart

import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter(private val dates: List<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        val index = value.toInt()

        if (index >= 0 && index < dates.size) {
            return dates[index]
        }

        return super.getFormattedValue(value)
    }
}