package com.myproject.dietproject.presentation.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.LoginFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private lateinit var mainActivity: MainActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callback: OnBackPressedCallback
    private lateinit var userId: String

    private var backPressedTime: Long = 0

    private val viewModel : LoginViewModel by viewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val curTime = System.currentTimeMillis()
                val gapTime = curTime - backPressedTime

                if(gapTime in 0..2000) {
                    Toast.makeText(requireContext(),"2초안에 누르면 종료?", Toast.LENGTH_SHORT).show()
                    mainActivity.finish()
                } else {
                    backPressedTime = curTime
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                }

            }
        }
        mainActivity.onBackPressedDispatcher.addCallback(this,callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


        //googleLogin() // 처음에 이걸로 로그인 되어있는지 안되어있는지 확인

        Log.d("LoginViewModel", "Login")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isInvisible = true

        binding.emailLoginButton.setOnClickListener {

            emailLogin()

        }

        binding.googleLoginButton.setOnClickListener {

            googleLogin()
            //signIn() // 버튼 누르면 로그인 유무에 따라 아이디 뜨던지 화면 넘어가던지

        }

        binding.signupButton.setOnClickListener {

            moveSignUpPage()

        }


    }

    private fun emailLogin() {

        if (binding.emailEdt.text.toString().isEmpty() || binding.pwEdt.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "이메일 혹은 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            signInEmail()
            //signInAndSignup()
        }

    }

    private fun signInEmail() { // 이메일 로그인하는 로직
        auth.signInWithEmailAndPassword(binding.emailEdt.text.toString(), binding.pwEdt.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(auth)
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun googleLogin() { // 이런 인증 절차도 결국 외부랑 네트워크 연결되는건데 다 data단으로 옮겨야하지 않을까?

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        Log.d("gsaId", gsa?.id.toString())

        if (gsa != null) {
            Toast.makeText(requireContext(), "로그인 되어있음", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "로그인 안되어있음", Toast.LENGTH_SHORT).show()
            signIn()
        }
    }


    private fun signIn() {

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN) // refactoring 해야함. 일단 미뤄둠.

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

    // 사용자 정보
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
                val userActivity = viewModel.loginUserActivity

                Log.d("viewModel_1", viewModel.loginUserActivity.value.toString())

            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) { // GoogleSignInAccount 객체에서 ID 토큰을 가져와서
        val credential = GoogleAuthProvider.getCredential(idToken, null) // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(requireContext(), "구글 로그인 성공", Toast.LENGTH_SHORT)
                        .show()

                    val user: FirebaseUser? = auth.currentUser

                    viewModel.getUserActivity(user?.uid.toString()) // 로그인 분기점을 위함

                    viewModel.loginUserActivity.observe(viewLifecycleOwner) {

                        if(it == null) {

                            movePersonalInfoPage(user?.uid.toString(), user?.email.toString())
                            viewModel.addUser(user?.uid.toString(), user?.email.toString())
                            Log.d("viewModel_2",viewModel.loginUserActivity.value.toString())

                        } else if (it == "hardActivity" || it == "middleActivity" || it == "lightActivity") {

                            Log.d("viewModel_3",viewModel.loginUserActivity.value.toString())
                            moveMainPage(auth)

                        }

                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "구글 로그인 실패", Toast.LENGTH_SHORT)
                        .show()
                    // updateUI(null);
                }
            }
    }


    private fun moveMainPage(auth: FirebaseAuth) {

        userId = auth.currentUser!!.uid
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(userId)
        findNavController().navigate(action)
        //Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)

    }

    private fun moveSignUpPage() {

        Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_signUpFragment)

    }

    private fun movePersonalInfoPage(userId: String, email: String) {
        val action = LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment(userId, email)
        findNavController().navigate(action)
    }


    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginFragmentTAG"
    }

}