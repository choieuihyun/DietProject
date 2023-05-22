package com.myproject.dietproject.presentation.ui.info

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("asdasdasd", auth.currentUser.toString())



        googleLogout()



    }

    private fun googleLogout() {

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

    }


}