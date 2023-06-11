package com.myproject.dietproject.presentation.ui.userkcal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalDetailFragment : BaseFragment<KcalDetailFragmentBinding>(R.layout.kcal_detail_fragment){

    private val args by navArgs<KcalDetailFragmentArgs>()
    private val viewModel : KcalDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kcal = args.kcal

        binding.makerName.text = kcal?.mAKERNAME
        binding.foodName.text = kcal?.dESCKOR
        binding.kcal.text = kcal?.nUTRCONT1
        binding.carbohydrate.text = kcal?.nUTRCONT2
        binding.protein.text = kcal?.nUTRCONT3
        binding.fat.text = kcal?.nUTRCONT4

        binding.plusButton.setOnClickListener {

            val serving = binding.servingEdittext.text.toString().toFloat()

            val servingResult = viewModel.plusServingCalculator(serving) // 이게 그 인분 쁠마

            val servingKcal = kcal?.nUTRCONT1

            if (servingKcal != null) {
                binding.kcal.text = viewModel.plusCalculator(servingKcal.toFloat() , servingResult).toString()
            }

            binding.servingEdittext.setText(servingResult.toString())

        }

        binding.minusButton.setOnClickListener {

            val serving = binding.servingEdittext.text.toString().toFloat()

            val servingResult = viewModel.minusServingCalculator(serving) // 몇인분 먹었는지 적는곳 마이너스 기능

            val servingKcal = kcal?.nUTRCONT1

            val kcalData = binding.kcal.text.toString().toFloat()

            if (servingKcal != null) {
                binding.kcal.text = viewModel.minusCalculator(kcalData, servingKcal.toFloat()).toString()
            }

            binding.servingEdittext.setText(servingResult.toString())

        }

        binding.dataInputButton.setOnClickListener {

            viewModel.addUserTodayKcal(args.userId, binding.kcal.text.toString().toFloat(), binding.foodName.text.toString(), binding.makerName.text.toString())

            Navigation.findNavController(binding.root).navigate(R.id.action_kcalDetailFragment_to_homeFragment)

        }


    }

}