package com.myproject.dietproject.presentation.ui.util

import android.content.Context
import com.myproject.dietproject.domain.error.NetworkError
import com.myproject.dietproject.presentation.R

fun NetworkError.toErrorMessage(
    context: Context
): String {

    return when (this) {
        is NetworkError.Unknown -> {
            context.getString(R.string.error_unknown)
        }
        is NetworkError.BadRequest -> {
            context.getString(R.string.error_bad_request, code)
        }
        is NetworkError.Timeout -> {
            context.getString(R.string.error_timeout)
        }
        is NetworkError.InternalServer -> {
            context.getString(R.string.error_internal_server)
        }

    }
}
