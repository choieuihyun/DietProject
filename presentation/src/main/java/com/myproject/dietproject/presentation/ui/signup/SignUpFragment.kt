package com.myproject.dietproject.presentation.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.SignupFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.login.LoginViewModel
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

                    val user: UserModel = UserModel( // view에서 model에 대해서 안다..음..
                        auth.uid.toString(),
                        binding.signupEmail.text.toString(),
                        24,
                        160.4F,
                        150.2F,
                        0,
                        null
                    )
                    loginViewModel.addUser(auth.uid.toString(), user)

                    loginViewModel.getUser(user.uid)

                    movePersonalInfoPage(task.result.user)

                    //moveMainPage(task.result?.user)

                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun movePersonalInfoPage(user: FirebaseUser?) {
        if(user != null) {
            Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_personalInfoFragment)
        }
    }


}