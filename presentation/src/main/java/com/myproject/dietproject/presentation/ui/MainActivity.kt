package com.myproject.dietproject.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.myproject.dietproject.presentation.BuildConfig
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var spalshScreen: SplashScreen
    private lateinit var auth: FirebaseAuth

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val getBinding get() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        KakaoSdk.init(this, BuildConfig.KAKAO_KEY)

        auth = Firebase.auth

        // spalshScreen = installSplashScreen()

        setContentView(binding.root)

        setupNavigation()

    }

    private fun setupNavigation() {

        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        navController.popBackStack()

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.isVisible = true

        val gsa = GoogleSignIn.getLastSignedInAccount(this@MainActivity)

        if (auth.currentUser?.uid != null) {
            goToHome()
        } else {
            Toast.makeText(this@MainActivity, "로그인 안되어있음", Toast.LENGTH_SHORT).show()
            goToLogin()
        }

    }

    private fun goToLogin() {

        navController.navigate(R.id.loginFragment,null)

    }

    private fun goToHome() {

        navController.navigate(R.id.homeFragment,null)

    }

}