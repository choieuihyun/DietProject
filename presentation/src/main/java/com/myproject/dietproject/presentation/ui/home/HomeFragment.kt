package com.myproject.dietproject.presentation.ui.home

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.HomeFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import com.myproject.dietproject.presentation.ui.util.BackPressedHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var circleProgressBar: CircularProgressIndicator

    private val args by navArgs<HomeFragmentArgs>() // 아 이거 home

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

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

        viewModel.getUserRecommendKcalData(auth.currentUser!!.uid)
        viewModel.getUserTodayKcalData(auth.currentUser!!.uid)

        viewModel.todayKcal.observe(viewLifecycleOwner) { todayKcal ->
            if (todayKcal != null) {
                viewModel.recommendKcal.observe(viewLifecycleOwner) { recommendKcal ->
                    if (recommendKcal != null) {
                        viewModel.getUserScarceKcalData(todayKcal)
                        progressBarSetting(todayKcal.toFloat(), recommendKcal.toFloat())
                    }
                }
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

        circleProgressBar = binding.circularProgressBar

        circleProgressBar.max = max.toInt()

        val animator = ValueAnimator.ofInt(0, sumKcal.toInt())
        animator.duration = 2000

        animator.addUpdateListener {
            circleProgressBar.setProgressCompat(sumKcal.toInt(), true)
        }

        animator.start()
        circleProgressBar.show()

    }

    private fun imageViewSetting() {

        if (viewModel.imageResultLiveData.value == 1)
            binding.homeFragmentImageView.setImageResource(R.drawable.hungry)
        else
            binding.homeFragmentImageView.setImageResource(R.drawable.obesity)

    }

}