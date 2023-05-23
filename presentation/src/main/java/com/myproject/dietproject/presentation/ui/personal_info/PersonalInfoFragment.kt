package com.myproject.dietproject.presentation.ui.personal_info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.PersonalInfoFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.login.LoginFragment
import com.myproject.dietproject.presentation.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<PersonalInfoFragmentBinding>(R.layout.personal_info_fragment) {

    private val args by navArgs<PersonalInfoFragmentArgs>()

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = args.userId


        buttonClick()




        }

    private fun buttonClick() {

        binding.maleButton.setOnClickListener {

            binding.femaleButton.isClickable = binding.femaleButton.isClickable != true

            if(binding.maleButton.isClickable) {
                binding.maleButton.isSelected = binding.maleButton.isSelected != true
            }
            Log.d("SSSSSSSSmale", binding.maleButton.isSelected.toString())
            Log.d("SSSSSSSSfemale", binding.femaleButton.isSelected.toString())
        }

        binding.femaleButton.setOnClickListener {

            binding.maleButton.isClickable = binding.maleButton.isClickable != true

            if(binding.femaleButton.isClickable) {
                binding.femaleButton.isSelected = binding.femaleButton.isSelected != true
            }
            Log.d("SSSSSmale", binding.maleButton.isSelected.toString())
            Log.d("SSSSSfemale", binding.femaleButton.isSelected.toString())
        }

        binding.lightActivity.setOnClickListener {
            binding.lightActivity.isSelected = binding.lightActivity.isSelected != true
        }

        binding.middleActivity.setOnClickListener {
            binding.middleActivity.isSelected = binding.middleActivity.isSelected != true
        }

        binding.hardActivity.setOnClickListener {
            binding.hardActivity.isSelected = binding.hardActivity.isSelected != true
        }

        binding.nextButton.setOnClickListener {

            if(!binding.maleButton.isSelected || !binding.femaleButton.isSelected) {

                if (binding.ageEditText.text.toString().isEmpty() || binding.heightEditText.text.toString().isEmpty() || binding.weightEditText.text.toString().isEmpty()
                ) {
                    if(!binding.lightActivity.isSelected || !binding.middleActivity.isSelected || !binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣어라", Toast.LENGTH_SHORT).show()
                        // loginViewModel.addUserInfo(userId, "male", 27, 174.2F, 82F, "low")
                    }
                }
            }

            if(binding.maleButton.isSelected || binding.femaleButton.isSelected) {

                if (binding.ageEditText.text.toString().isNotEmpty() && binding.heightEditText.text.toString().isNotEmpty() && binding.weightEditText.text.toString().isNotEmpty()
                ) {
                    if(binding.lightActivity.isSelected || binding.middleActivity.isSelected || binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣었네", Toast.LENGTH_SHORT).show()
                        // loginViewModel.addUserInfo(userId, "male", 27, 174.2F, 82F, "low")
                    }
                }
            }

        }

    }

}