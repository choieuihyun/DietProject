package com.myproject.dietproject.presentation.ui.personal_info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.PersonalInfoFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<PersonalInfoFragmentBinding>(R.layout.personal_info_fragment) {

    private val args by navArgs<PersonalInfoFragmentArgs>()

    private val loginViewModel: PersonalInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = args.userId

        personalInfoWork(userId)

    }

    private fun personalInfoWork(userId: String) {

        buttonSetting()

        binding.nextButton.setOnClickListener {

            if(!binding.maleButton.isSelected || !binding.femaleButton.isSelected) {

                if (binding.ageEditText.text.toString().isEmpty() || binding.heightEditText.text.toString().isEmpty() || binding.weightEditText.text.toString().isEmpty()
                ) {
                    if(!binding.lightActivity.isSelected || !binding.middleActivity.isSelected || !binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣어라", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if(binding.maleButton.isSelected || binding.femaleButton.isSelected) {

                if (binding.ageEditText.text.toString().isNotEmpty() && binding.heightEditText.text.toString().isNotEmpty() && binding.weightEditText.text.toString().isNotEmpty()
                ) {
                    if(binding.lightActivity.isSelected || binding.middleActivity.isSelected || binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣었네", Toast.LENGTH_SHORT).show()

                        loginViewModel.setAgeInfo(binding.ageEditText.text.toString().toInt())
                        loginViewModel.setHeightInfo(binding.heightEditText.text.toString().toFloat())
                        loginViewModel.setWeightInfo(binding.weightEditText.text.toString().toFloat())

                        loginViewModel.addUserInfo(userId, loginViewModel.userGender, loginViewModel.userAge, loginViewModel.userHeight, loginViewModel.userWeight, loginViewModel.userActivity)
                        moveHomeFragment(userId)
                    }
                }
            }
        }
    }

    private fun buttonSetting() {

        binding.maleButton.setOnClickListener {

            binding.femaleButton.isClickable = binding.femaleButton.isClickable != true

            if(binding.maleButton.isClickable) {
                binding.maleButton.isSelected = binding.maleButton.isSelected != true
                loginViewModel.setGenderInfo("male")
            }
        }

        binding.femaleButton.setOnClickListener {

            binding.maleButton.isClickable = binding.maleButton.isClickable != true

            if(binding.femaleButton.isClickable) {
                binding.femaleButton.isSelected = binding.femaleButton.isSelected != true
                loginViewModel.setGenderInfo("female")
            }
        }

        binding.lightActivity.setOnClickListener {

            binding.middleActivity.isClickable = binding.middleActivity.isClickable != true
            binding.hardActivity.isClickable = binding.hardActivity.isClickable != true

            if(binding.lightActivity.isClickable) {
                binding.lightActivity.isSelected = binding.lightActivity.isSelected != true
                loginViewModel.setActivityInfo("lightActivity")
            }
        }

        binding.middleActivity.setOnClickListener {

            binding.lightActivity.isClickable = binding.lightActivity.isClickable != true
            binding.hardActivity.isClickable = binding.hardActivity.isClickable != true

            if(binding.middleActivity.isClickable) {
                binding.middleActivity.isSelected = binding.middleActivity.isSelected != true
                loginViewModel.setActivityInfo("middleActivity")
            }
        }

        binding.hardActivity.setOnClickListener {

            binding.lightActivity.isClickable = binding.lightActivity.isClickable != true
            binding.middleActivity.isClickable = binding.middleActivity.isClickable != true

            if(binding.hardActivity.isClickable) {
                binding.hardActivity.isSelected = binding.hardActivity.isSelected != true
                loginViewModel.setActivityInfo("hardActivity")
            }
        }

    }

    private fun moveHomeFragment(userId: String) {
        val action = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToHomeFragment(userId)
        findNavController().navigate(action)
    }

}