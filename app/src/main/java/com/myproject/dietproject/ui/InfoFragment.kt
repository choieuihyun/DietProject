package com.myproject.dietproject.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.R
import com.myproject.dietproject.databinding.InfoFragmentBinding

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

        binding.googleLogoutButton.setOnClickListener {

            auth.signOut()

            Navigation.findNavController(binding.root).navigate(R.id.action_myInfoFragment_to_loginFragment)

        }

    }


}