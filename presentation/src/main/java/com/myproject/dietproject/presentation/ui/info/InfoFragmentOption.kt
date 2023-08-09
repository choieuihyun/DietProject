package com.myproject.dietproject.presentation.ui.info

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.InfoFragmentOptionBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragmentOption : BaseFragment<InfoFragmentOptionBinding>(R.layout.info_fragment_option){

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        mainActivity.getBinding.bottomNavigationView.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        googleLogout()

    }

    override fun onPause() {
        super.onPause()



    }


    private fun googleLogout() {

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.logoutButton.setOnClickListener {

            mGoogleSignInClient.signOut() // 재 로그인시 다시 구글 아이디 고르게
            auth.signOut() // 그냥 로그아웃

            Navigation.findNavController(binding.root).navigate(R.id.action_infoFragmentDetail_to_loginFragment)

        }

    }

}