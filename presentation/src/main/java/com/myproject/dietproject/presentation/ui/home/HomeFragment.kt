package com.myproject.dietproject.presentation.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.HomeFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException

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

        viewModel.getUserTodayKcalData(auth.currentUser!!.uid)
        viewModel.getRecommendKcalData(auth.currentUser!!.uid)

        viewModel.todayKcal.observe(viewLifecycleOwner) {

            progressBarSetting(it)
            binding.todayKcal.text = it.toInt().toString() + " Kcal"
            Log.d("homeFragment1", it.toString())

        }

        viewModel.recommendKcal.observe(viewLifecycleOwner) {

            binding.recommendKcal.text = "$it Kcal"
            Log.d("homeFragment2", it.toString())

        }

        viewModel.scarceKcal.observe(viewLifecycleOwner) {

            Log.d("homeFragment3", it.toString())
            binding.scarceKcal.text = "$it Kcal"

        }


        binding.setDataButton.setOnClickListener {

            val action = HomeFragmentDirections.actionHomeFragmentToKcalFragment(auth.currentUser!!.uid)
            findNavController().navigate(action)

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