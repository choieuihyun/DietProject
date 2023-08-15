package com.myproject.dietproject.presentation.ui.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.InfoFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<InfoFragmentBinding>(R.layout.info_fragment) {

    private lateinit var auth: FirebaseAuth
    private val viewModel: InfoViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    private val PICK_IMAGE_REQUEST = 1

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {

            val data = result.data
            val imageUri = data?.data
            binding.profileImage.setImageURI(imageUri)
            Log.d("URIstring", imageUri.toString())

            if (imageUri != null) {
                viewModel.addUserProfileImage(auth.currentUser!!.uid, "profileImages", imageUri)
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.getUserName(auth.currentUser!!.uid)
        viewModel.getUserEmail(auth.currentUser!!.uid)
        viewModel.getUserRecommendKcal(auth.currentUser!!.uid)
        viewModel.getUserTargetWeight(auth.currentUser!!.uid)
        viewModel.getUserMaxKcal(auth.currentUser!!.uid)
        viewModel.getMostNumerousDate(auth.currentUser!!.uid)

        viewModel.getUserProfileImage(requireContext(),
            auth.currentUser!!.uid,
            "profileImages",
             binding.profileImage,
             R.drawable.profile_nothing
        )

        binding.settingButton.setOnClickListener {

            Navigation.findNavController(binding.root).navigate(R.id.action_InfoFragment_to_OptionFragment)

        }

        binding.profileImage.setOnClickListener {

            openGallery()

        }

        binding.profileImageButton.setOnClickListener {

            openGallery()

        }

    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }






}


