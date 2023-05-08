package com.myproject.dietproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.myproject.dietproject.R
import com.myproject.dietproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var spalshScreen: SplashScreen

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val getBinding get() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        spalshScreen = installSplashScreen()

        setContentView(binding.root)

        setupNavigation()

    }

    private fun setupNavigation() {

        val host = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment? ?: return
        navController = host.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.isVisible = true

    }

}