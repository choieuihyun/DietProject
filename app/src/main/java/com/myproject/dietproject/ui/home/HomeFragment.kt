package com.myproject.dietproject.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.myproject.dietproject.R
import com.myproject.dietproject.databinding.HomeFragmentBinding
import com.myproject.dietproject.ui.BaseFragment
import com.myproject.dietproject.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel : HomeViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    // private val args by navArgs<HomeFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = true

        binding.appCompatButton.setOnClickListener {

            viewModel.getKcalData("감자")
            viewModel.kcalData.observe(viewLifecycleOwner) {
                if (it != null) {

                    progressBarSetting(viewModel.kcalData.value?.get(0)?.nUTRCONT1?.toFloat()
                        ?: 0.0f)

                }
            }
        }
    }

    private fun progressBarSetting(sumKcal: Float) {

        val circleProgressBar = binding.circularProgressBar

        circleProgressBar.apply {
            // Set Progress
            progress = sumKcal

            // or with animation 이게 xml에 없던데
            setProgressWithAnimation(progress, 1500)


        }
    }
}