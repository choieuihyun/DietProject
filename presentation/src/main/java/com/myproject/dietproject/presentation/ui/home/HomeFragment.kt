package com.myproject.dietproject.presentation.ui.home

import android.animation.ValueAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.impl.background.systemalarm.SystemAlarmService
import androidx.work.impl.background.systemjob.SystemJobService
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.HomeFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import com.myproject.dietproject.presentation.ui.util.BackPressedHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var auth: FirebaseAuth
    private lateinit var circleProgressBar: CircularProgressIndicator

    private val args by navArgs<HomeFragmentArgs>() // 아 이거 home

    val pushID = "1"
    val pushChannel = "푸시 알림"

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        createNotificationChannel()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BackPressedHandler.handleBackPress(this)

        binding.homeFragmentViewModel = viewModel

        viewModel.getUserRecommendKcalData(auth.currentUser!!.uid)
        viewModel.getUserTodayKcalData(auth.currentUser!!.uid)

        viewModel.todayKcal.observe(viewLifecycleOwner) { todayKcal ->
            if (todayKcal != null) {
                viewModel.recommendKcal.observe(viewLifecycleOwner) { recommendKcal ->
                    if (recommendKcal != null) {
                        viewModel.getUserScarceKcalData(todayKcal)
                        progressBarSetting(todayKcal.toFloat(), recommendKcal.toFloat())
                    }


                }
            }
        }

        viewModel.scarceKcal.observe(viewLifecycleOwner) {

            if(it < 0) {

                val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, buildNotification(abs(it)))

            }

        }

        viewModel.imageResultLiveData.observe(viewLifecycleOwner) {

            imageViewSetting()

        }

        binding.setDataButton.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToKcalFragment(auth.currentUser!!.uid)
            findNavController().navigate(action)

        }

        binding.previousButton.setOnClickListener {

            viewModel.movePreviousDate(auth.currentUser!!.uid)

        }

        binding.nextButton.setOnClickListener {

            viewModel.moveNextDate(auth.currentUser!!.uid)

        }

        createNotificationChannel()
        deleteChannel()


    }

    override fun onResume() {
        super.onResume()

        mainActivity.getBinding.bottomNavigationView.isVisible = true

    }

    private fun progressBarSetting(sumKcal: Float, max: Float) {

        circleProgressBar = binding.circularProgressBar

        circleProgressBar.max = max.toInt()

        val animator = ValueAnimator.ofInt(0, sumKcal.toInt())
        animator.duration = 2000

        animator.addUpdateListener {
            circleProgressBar.setProgressCompat(sumKcal.toInt(), true)
        }

        animator.start()
        circleProgressBar.show()
    }

    private fun imageViewSetting() {

        if (viewModel.imageResultLiveData.value == 1)
            binding.homeFragmentImageView.setImageResource(R.drawable.hungry)
        else
            binding.homeFragmentImageView.setImageResource(R.drawable.obesity)

    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            pushID,
            pushChannel,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = requireContext().getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

    }

    private fun buildNotification(kcal: Int): Notification {

        val builder = NotificationCompat.Builder(requireContext(), pushID)
            .setContentTitle("In Your Life")
            .setContentText("$kcal 초과했어요")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        return builder

    }

    private fun deleteChannel() {

        val manager = requireContext().getSystemService(NotificationManager::class.java)
        manager.deleteNotificationChannel("10000")

    }

    private fun modifyChannel(channelID: String) {

        val manager = requireContext().getSystemService(NotificationManager::class.java)
        val channel = manager.getNotificationChannel(channelID)
        channel.name = channelID
        manager.createNotificationChannel(channel)

    }

}