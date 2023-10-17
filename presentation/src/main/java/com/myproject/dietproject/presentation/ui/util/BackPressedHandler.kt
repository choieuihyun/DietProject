package com.myproject.dietproject.presentation.ui.util

import android.os.Handler
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

object BackPressedHandler {
    private var doubleBackToExitPressedOnce = false

    fun handleBackPress(fragment: Fragment) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    fragment.requireActivity().finish() // 앱 종료
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(fragment.requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 1500) // 2초 동안 뒤로가기 버튼을 한 번 더 누를 수 있도록 설정
                }
            }
        }

        fragment.requireActivity().onBackPressedDispatcher.addCallback(fragment.viewLifecycleOwner, callback)
    }
}