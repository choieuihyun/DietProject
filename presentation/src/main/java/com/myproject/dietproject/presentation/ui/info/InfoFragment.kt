package com.myproject.dietproject.presentation.ui.info

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.InfoFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<InfoFragmentBinding>(R.layout.info_fragment) {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("asdasdasd", auth.currentUser.toString())

        binding.viewModel = viewModel

        viewModel.getUserEmail(auth.currentUser!!.uid)
        viewModel.getUserRecommendKcal(auth.currentUser!!.uid)
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

    override fun onDestroy() {
        super.onDestroy()

    }





}

/*    private fun googleLogout() {

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.googleLogoutButton.setOnClickListener {

            mGoogleSignInClient.signOut() // 재 로그인시 다시 구글 아이디 고르게
            auth.signOut() // 그냥 로그아웃

            Navigation.findNavController(binding.root).navigate(R.id.action_myInfoFragment_to_loginFragment)

        }

    }*/

