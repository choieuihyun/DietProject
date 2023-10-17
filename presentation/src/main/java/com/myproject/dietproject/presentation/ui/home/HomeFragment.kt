package com.myproject.dietproject.presentation.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.Utility
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.HomeFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel: HomeViewModel by viewModels()
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

        var keyHash = Utility.getKeyHash(requireContext())
        Log.i("asdfasdf", "$keyHash")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BackPressedHandler.handleBackPress(this)

        binding.homeFragmentViewModel = viewModel

        viewModel.getRecommendKcalData(auth.currentUser!!.uid)
        viewModel.getUserTodayKcalData(auth.currentUser!!.uid)

        viewModel.todayKcal.observe(viewLifecycleOwner) { todayKcal ->
            viewModel.recommendKcal.observe(viewLifecycleOwner) { recommendKcal -> // recommendKcal가 변화가 없어서 지속적으로 관찰할 수 없음.
                // 그래서 todayKcal 안에 두면 될 것 같았는데 안그래도 되긴 함. 어차피 변동 없는 값이라.
                Log.d("homeFragmentTodayKcal", todayKcal.toString())
                progressBarSetting(todayKcal.toFloat(), recommendKcal.toFloat())
                Log.d(
                    "homeFragmentRecommendKcal",
                    viewModel.recommendKcal.value?.toFloat().toString()
                )

            }
        }

        viewModel.scarceKcal.observe(viewLifecycleOwner) {


        }

        viewModel.imageResultLiveData.observe(viewLifecycleOwner) {

            imageViewSetting()

        }

        binding.setDataButton.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToKcalFragment(auth.currentUser!!.uid)
            findNavController().navigate(action)

        }

        binding.previousButton.setOnClickListener {

            viewModel.movePreviousDate(auth.currentUser!!.uid)

        }

        binding.nextButton.setOnClickListener {

            viewModel.moveNextDate(auth.currentUser!!.uid)

        }

    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    private fun progressBarSetting(sumKcal: Float, max: Float) {

        val circleProgressBar = binding.circularProgressBar

        circleProgressBar.apply {
            progress = 0.0F

            progressMax = max
            // Set Progress
            progress = sumKcal
        }
    }

    private fun imageViewSetting() {

        if (viewModel.imageResultLiveData.value == 1)
            binding.homeFragmentImageView.setImageResource(R.drawable.hungry)
        else
            binding.homeFragmentImageView.setImageResource(R.drawable.obesity)

    }

}