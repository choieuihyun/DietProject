package com.myproject.dietproject.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.R
import com.myproject.dietproject.data.db.remote.response.kcalresponse.User
import com.myproject.dietproject.databinding.SignupFragmentBinding
import com.myproject.dietproject.ui.BaseFragment
import com.myproject.dietproject.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignupFragmentBinding>(R.layout.signup_fragment) {

    private lateinit var auth: FirebaseAuth

    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {

            signUpEmail()

        }
    }

    private fun signUpEmail() {

        auth.createUserWithEmailAndPassword(binding.signupEmail.text.toString(), binding.signupPw.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {

                    val user: User = User(
                        auth.uid.toString(),
                        binding.signupEmail.text.toString(),
                        null,
                        null
                    )
                    loginViewModel.getUserDB(auth.uid.toString(), user)

                    moveMainPage(task.result?.user)

                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if(user != null) {
            Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_homeFragment)

        }
    }


}