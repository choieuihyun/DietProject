package com.myproject.dietproject.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
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

//        binding.bottomNavigationView.setOnClickListener {
//            when(it.id) {
//                R.id.calendarFragment -> {
//                    Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_calendarFragment)
//                    Log.d("asdasdasd","asdasdasd")
//                }
//            }
//        }

    }

    private fun setupNavigation() {

        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.isVisible = true

    }

}