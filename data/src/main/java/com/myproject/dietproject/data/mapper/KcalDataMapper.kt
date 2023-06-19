package com.myproject.dietproject.data.mapper

import com.myproject.dietproject.data.db.remote.response.kcalresponse.KcalData
import com.myproject.dietproject.domain.error.NetworkResult

import com.myproject.dietproject.domain.model.Kcal

fun KcalData.toModel() = Kcal(

        dESCKOR = dESCKOR,
        mAKERNAME = mAKERNAME,
        nUTRCONT1 = nUTRCONT1,
        nUTRCONT2 = nUTRCONT2,
        nUTRCONT3 = nUTRCONT3,
        nUTRCONT4 = nUTRCONT4,
        sERVINGSIZE = sERVINGSIZE

    )

fun <T> NetworkResult<T>.toNetworkResult() : T =
    (this as NetworkResult.Success).data

private fun <R> changeNetworkData(replaceData: R) : NetworkResult<R> {
    return NetworkResult.Success(replaceData)
}

suspend fun <T, R> NetworkResult<T>.mapNetworkResult(getData: suspend (T) -> R): NetworkResult<R> {
    return changeNetworkData(getData(toNetworkResult()))
}

