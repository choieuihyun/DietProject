package com.myproject.dietproject.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.R
import com.myproject.dietproject.databinding.LoginFragmentBinding
import com.myproject.dietproject.ui.BaseFragment
import com.myproject.dietproject.ui.MainActivity


class LoginFragment : BaseFragment<LoginFragmentBinding>(com.myproject.dietproject.R.layout.login_fragment) {

    private lateinit var mainActivity: MainActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        googleLogin()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isInvisible = true

        binding.emailLoginButton.setOnClickListener {
            emailLogin()
        }

        binding.googleLoginButton.setOnClickListener {

            signIn()
        }
    }


    private fun emailLogin() {

        if(binding.emailEdt.text.toString().isEmpty() || binding.pwEdt.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "이메일 혹은 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            signInAndSignup()
        }

    }

    private fun signInAndSignup() {
        auth.createUserWithEmailAndPassword(binding.emailEdt.text.toString(), binding.pwEdt.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    signInEmail()
                }
            }
    }

    private fun signInEmail() {
        auth.signInWithEmailAndPassword(binding.emailEdt.text.toString(),
            binding.pwEdt.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun googleLogin() {

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())


        if(gsa != null) {
            Toast.makeText(requireContext(), "로그인 되어있음", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(requireContext(), "로그인 안되어있음", Toast.LENGTH_SHORT).show()
            signIn()

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN) // refactoring 해야함. 일단 미뤄둠.
    }

    private fun signOut() { // 이건 내 정보에 올라가야 하는거 아님?

        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                auth.signOut()
                Log.d("TAG", "로그아웃되었음")
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account != null) {
                account.idToken?.let {
                    firebaseAuthWithGoogle(it)
                }
                val personName = account.displayName
                val personGivenName = account.givenName
                val personFamilyName = account.familyName
                val personEmail = account.email
                val personId = account.id
                val personPhoto: Uri? = account.photoUrl
                Log.d(TAG, "handleSignInResult:personName $personName")
                Log.d(TAG, "handleSignInResult:personGivenName $personGivenName")
                Log.d(TAG, "handleSignInResult:personEmail $personEmail")
                Log.d(TAG, "handleSignInResult:personId $personId")
                Log.d(TAG, "handleSignInResult:personFamilyName $personFamilyName")
                Log.d(TAG, "handleSignInResult:personPhoto $personPhoto")
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(requireContext(), "구글 로그인 성공", Toast.LENGTH_SHORT)
                        .show()
                    val user: FirebaseUser? = auth.currentUser
                    Log.d(TAG,auth.currentUser.toString())
                    moveMainPage(user)
                    //                            updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "구글 로그인 실패", Toast.LENGTH_SHORT)
                        .show()
                    //                            updateUI(null);
                }
            }
    }


    private fun moveMainPage(user: FirebaseUser?) {
        if(user != null) {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)

        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginFragmentTAG"
    }

}