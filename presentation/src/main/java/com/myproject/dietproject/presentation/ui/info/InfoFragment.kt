package com.myproject.dietproject.presentation.ui.info

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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

        Log.d("asdasdasd", auth.currentUser.toString())

        binding.viewModel = viewModel

        viewModel.getUserName(auth.currentUser!!.uid)
        viewModel.getUserEmail(auth.currentUser!!.uid)
        viewModel.getUserRecommendKcal(auth.currentUser!!.uid)
        viewModel.getUserTargetWeight(auth.currentUser!!.uid)
        viewModel.getUserMaxKcal(auth.currentUser!!.uid)
        viewModel.getMostNumerousDate(auth.currentUser!!.uid)



//        viewModel.email.observe(viewLifecycleOwner) {
//            binding.profileEmail.text = it
//        }


        // googleLogout()

        binding.settingButton.setOnClickListener {

            Navigation.findNavController(binding.root).navigate(R.id.action_InfoFragment_to_infoFragmentDetail)

        }


    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    override fun onDestroy() {
        super.onDestroy()

    }





}


