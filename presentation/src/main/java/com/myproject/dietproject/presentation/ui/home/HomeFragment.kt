package com.myproject.dietproject.presentation.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.HomeFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel : HomeViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var auth: FirebaseAuth

    private val args by navArgs<HomeFragmentArgs>() // 아 이거 home

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      //  val userId = args.userId

            binding.dbTest.setOnClickListener {

            viewModel.getKcalData("감자")

            viewModel.kcalData.observe(viewLifecycleOwner) {
                if (it != null) {

                    progressBarSetting(viewModel.kcalData.value?.get(0)?.nUTRCONT1?.toFloat()
                        ?: 0.0f) // 여긴 엘비스 연산자로 했고 근데 에러처리도 어차피 여기서 하는게 아니지 않을까?

                    viewModel.addUserTodayKcal(
                        auth.currentUser!!.uid,
                        viewModel.kcalData.value?.get(0)?.nUTRCONT1?.toFloat()!!,
                        viewModel.kcalData.value?.get(0)?.dESCKOR.toString()
                    ) // non assert 제거 요망

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