package com.myproject.dietproject.presentation.ui.userkcal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalDetailFragment : BaseFragment<KcalDetailFragmentBinding>(R.layout.kcal_detail_fragment){

    private val args by navArgs<KcalDetailFragmentArgs>()
    private val viewModel : KcalDetailViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kcal = args.kcal // KcalFragment에서 현지 직송해온 데이터로 바로 넣음.
        val kcalByFoodDiary = args.fooddiary

        when {
            args.kcal != null -> {
                viewModel.setKcalData(kcal)
            }
            args.fooddiary != null -> {
                viewModel.setFoodDiaryData(kcalByFoodDiary)
            }

        }


        binding.kcalDetailData = args.kcal
        binding.kcalDetailViewModel = viewModel

        viewModel.getDate(args.userId)

        binding.previousButton.setOnClickListener {

            viewModel.movePreviousDate(args.userId)

        }

        binding.nextButton.setOnClickListener {

            viewModel.moveNextDate(args.userId)

        }

        binding.plusButton.setOnClickListener {

            val serving = binding.servingEdittext.text.toString().toFloat()

            val servingResult = viewModel.plusServingCalculator(serving) // 이게 그 인분 쁠마

            val servingKcal = kcal?.nUTRCONT1

            if (servingKcal != null) {
                binding.kcal.text = viewModel.plusCalculator(servingKcal.toFloat() , servingResult).toInt().toString()
            }

            binding.servingEdittext.setText(servingResult.toString())

        }

        binding.minusButton.setOnClickListener {

            val serving = binding.servingEdittext.text.toString().toFloat() // 인분

            val servingResult = viewModel.minusServingCalculator(serving) // 몇인분 먹었는지 적는곳 마이너스 기능

            val servingKcal = kcal?.nUTRCONT1

            val kcalData = binding.kcal.text.toString().toFloat()

            if (servingKcal != null) {
                binding.kcal.text = viewModel.minusCalculator(kcalData, servingKcal.toFloat()).toInt().toString()
            }

            binding.servingEdittext.setText(servingResult.toString())

        }

        binding.dataInputButton.setOnClickListener {

            viewModel.addUserTodayKcal(args.userId,
                binding.kcal.text.toString().toFloat(),
                binding.foodName.text.toString(),
                binding.makerName.text.toString(),
                viewModel.kcalDetailDataByDate.value.toString()
            )

            Log.d("inputDate", viewModel.kcalDetailDataByDate.value.toString())

            Navigation.findNavController(binding.root).navigate(R.id.action_kcalDetailFragment_to_homeFragment)

        }


    }

    override fun onPause() {
        super.onPause()


    }

}