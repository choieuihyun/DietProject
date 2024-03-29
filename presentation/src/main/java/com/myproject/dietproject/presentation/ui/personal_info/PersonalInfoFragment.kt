package com.myproject.dietproject.presentation.ui.personal_info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.PersonalInfoFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<PersonalInfoFragmentBinding>(R.layout.personal_info_fragment) {

    private lateinit var auth: FirebaseAuth

    private val args by navArgs<PersonalInfoFragmentArgs>()

    private val personalInfoViewModel: PersonalInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = args.email
        val pw = args.pw

        personalInfoWork(email, pw)

    }

    private fun personalInfoWork(email: String, pw: String) {

        buttonSetting()

        binding.nextButton.setOnClickListener {

            if(!binding.maleButton.isSelected || !binding.femaleButton.isSelected) {

                if (binding.nameEditText.text.toString().isEmpty() ||
                    binding.ageEditText.text.toString().isEmpty() ||
                    binding.heightEditText.text.toString().isEmpty() ||
                    binding.weightEditText.text.toString().isEmpty() ||
                    binding.targetWeightEditText.text.toString().isEmpty()
                ) {
                    if(!binding.lightActivity.isSelected || !binding.middleActivity.isSelected || !binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣어라", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if(binding.maleButton.isSelected || binding.femaleButton.isSelected) {

                if (binding.ageEditText.text.toString().isNotEmpty() &&
                    binding.nameEditText.text.toString().isNotEmpty() &&
                    binding.heightEditText.text.toString().isNotEmpty() &&
                    binding.weightEditText.text.toString().isNotEmpty() &&
                    binding.targetWeightEditText.text.toString().isNotEmpty()
                ) {
                    if(binding.lightActivity.isSelected || binding.middleActivity.isSelected || binding.hardActivity.isSelected) {
                        Toast.makeText(requireContext(), "데이터 넣었네", Toast.LENGTH_SHORT).show()

                        if(auth.currentUser?.email == null) {

                            auth.createUserWithEmailAndPassword(email, pw)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {

                                        personalInfoViewModel.addUser(auth.uid.toString(), email)

                                        personalInfoViewModel.getUser(auth.uid.toString())

                                        personalInfoViewModel.setNameInfo(binding.nameEditText.text.toString())

                                        personalInfoViewModel.setAgeInfo(
                                            binding.ageEditText.text.toString().toInt()
                                        )
                                        personalInfoViewModel.setHeightInfo(
                                            binding.heightEditText.text.toString().toFloat()
                                        )
                                        personalInfoViewModel.setWeightInfo(
                                            binding.weightEditText.text.toString().toFloat()
                                        )
                                        personalInfoViewModel.setTargetWeightInfo(
                                            binding.weightEditText.text.toString().toFloat()
                                        )

                                        personalInfoViewModel.addUserInfo(auth.uid.toString())

                                        moveHomeFragment(auth.uid.toString())

                                    } else if (task.exception?.message.isNullOrEmpty()) {
                                        Toast.makeText(
                                            requireContext(),
                                            task.exception?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                        } else {

                            personalInfoViewModel.setNameInfo(
                                binding.nameEditText.text.toString()
                            )

                            personalInfoViewModel.setAgeInfo(
                                binding.ageEditText.text.toString().toInt()
                            )
                            personalInfoViewModel.setHeightInfo(
                                binding.heightEditText.text.toString().toFloat()
                            )
                            personalInfoViewModel.setWeightInfo(
                                binding.weightEditText.text.toString().toFloat()
                            )
                            personalInfoViewModel.setTargetWeightInfo(
                                binding.weightEditText.text.toString().toFloat()
                            )

                            personalInfoViewModel.addUserInfo(auth.uid.toString())

                            moveHomeFragment(auth.uid.toString())

                        }
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
                personalInfoViewModel.setGenderInfo("male")
            }
        }

        binding.femaleButton.setOnClickListener {

            binding.maleButton.isClickable = binding.maleButton.isClickable != true

            if(binding.femaleButton.isClickable) {
                binding.femaleButton.isSelected = binding.femaleButton.isSelected != true
                personalInfoViewModel.setGenderInfo("female")
            }
        }

        binding.lightActivity.setOnClickListener {

            binding.middleActivity.isClickable = binding.middleActivity.isClickable != true
            binding.hardActivity.isClickable = binding.hardActivity.isClickable != true

            if(binding.lightActivity.isClickable) {
                binding.lightActivity.isSelected = binding.lightActivity.isSelected != true
                personalInfoViewModel.setActivityInfo("lightActivity")
            }
        }

        binding.middleActivity.setOnClickListener {

            binding.lightActivity.isClickable = binding.lightActivity.isClickable != true
            binding.hardActivity.isClickable = binding.hardActivity.isClickable != true

            if(binding.middleActivity.isClickable) {
                binding.middleActivity.isSelected = binding.middleActivity.isSelected != true
                personalInfoViewModel.setActivityInfo("middleActivity")
            }
        }

        binding.hardActivity.setOnClickListener {

            binding.lightActivity.isClickable = binding.lightActivity.isClickable != true
            binding.middleActivity.isClickable = binding.middleActivity.isClickable != true

            if(binding.hardActivity.isClickable) {
                binding.hardActivity.isSelected = binding.hardActivity.isSelected != true
                personalInfoViewModel.setActivityInfo("hardActivity")
            }
        }

    }


    private fun moveHomeFragment(userId: String) {

        val action = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToHomeFragment(userId)
        findNavController().navigate(action)
    }

}