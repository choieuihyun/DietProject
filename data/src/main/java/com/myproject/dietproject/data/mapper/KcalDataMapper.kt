package com.myproject.dietproject.data.mapper

import com.myproject.dietproject.data.db.remote.response.kcalresponse.KcalData
import com.myproject.dietproject.domain.error.NetworkResult

import com.myproject.dietproject.domain.model.Kcal
import java.lang.NumberFormatException
import kotlin.math.roundToInt

fun KcalData.toModel() = Kcal(

        dESCKOR = dESCKOR,
        mAKERNAME = mAKERNAME,
        nUTRCONT1 = convertStringToInt(nUTRCONT1.toString()),
        nUTRCONT2 = convertStringToInt(nUTRCONT2.toString()),
        nUTRCONT3 = convertStringToInt(nUTRCONT3.toString()),
        nUTRCONT4 = convertStringToInt(nUTRCONT4.toString()),
        sERVINGSIZE = sERVINGSIZE

    )


private fun convertStringToInt(value: String) : String {

    var stringValue = ""

    try {

        val convertValue = value.toFloat().roundToInt()

        stringValue = convertValue.toString()

    } catch (n : NumberFormatException) {

    }

    return stringValue
}


fun <T> NetworkResult<T>.toNetworkResult() : T =
    (this as NetworkResult.Success).data

private fun <R> changeNetworkData(replaceData: R) : NetworkResult<R> {
    return NetworkResult.Success(replaceData)
}

suspend fun <T, R> NetworkResult<T>.mapNetworkResult(getData: suspend (T) -> R): NetworkResult<R> {
    return changeNetworkData(getData(toNetworkResult()))
}

