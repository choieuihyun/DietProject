package com.myproject.dietproject.ui.util

import android.content.Context
import android.widget.Toast
import com.myproject.dietproject.R
import com.myproject.dietproject.domain.error.NetworkError

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
