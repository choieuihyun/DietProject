package com.myproject.dietproject.presentation.ui.calendar

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.CalendarDetailFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalenderDetailFragment : BaseFragment<CalendarDetailFragmentBinding>(R.layout.calendar_detail_fragment) {

    private lateinit var calendarDetailAdapter: CalendarDetailAdapter
    private val args by navArgs<CalenderDetailFragmentArgs>()
    private lateinit var mainActivity: MainActivity
    private val viewModel: CalendarDetailViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.noteDate.text = args.date

        binding.viewModel = viewModel

        viewModel.getCalendarDetailData(args.userId, args.date.toString()) // null 경우 처리
        viewModel.getRecommendKcalData(args.userId)

        viewModel.dayKcalList.observe(viewLifecycleOwner) {
            calendarDetailAdapter.submitList(it)
        }

        viewModel.imageResultLiveData.observe(viewLifecycleOwner) {
            imageViewSetting()
        }

        kakaoTest()

        binding.messageButton.setOnClickListener {
            messageShared()
        }
    }

    override fun onPause() {
        super.onPause()

    }

    private fun setupRecyclerView() {

        calendarDetailAdapter = CalendarDetailAdapter()

        binding.calendarDetailRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = calendarDetailAdapter
        }

    }

    private fun imageViewSetting() {

        if (viewModel.imageResultLiveData.value == 1)

            binding.calendarDetailImageView.setImageResource(R.drawable.calendar_detail_weight)

        else

            binding.calendarDetailImageView.setImageResource(R.drawable.calendar_detail_good)

    }

    private fun messageShared() {

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, data)
        }
        startActivity(Intent.createChooser(intent, "data"))

        when(intent.action) {
            Intent.ACTION_SEND -> {
                if(intent.type == "text/plain") {
                    ""
                }
            }
        }
    }

    private fun kakaoTest() {

        val defaultFeed = FeedTemplate(
            content = Content(
                title = "타이틀",
                description = "설명~~~~~~~~",
                imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            buttons = listOf(
                Button(
                    "웹으로 보기",
                    Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
                    )
                ),
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                        iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                    )
                )
            )
        )

        binding.kakaoTalkButton.setOnClickListener {
            // 카카오톡 설치여부 확인
            if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
                // 카카오톡으로 카카오톡 공유 가능
                ShareClient.instance.shareDefault(requireContext(), defaultFeed) { sharingResult, error ->
                    if (error != null) {
                        Log.e("sdfsdfsdf", "카카오톡 공유 실패", error)
                    }
                    else if (sharingResult != null) {
                        Log.d("sdfsdfsdf", "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)

                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w("sdfsdfsdf", "Warning Msg: ${sharingResult.warningMsg}")
                        Log.w("sdfsdfsdf", "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // 카카오톡 미설치: 웹 공유 사용 권장
                // 웹 공유 예시 코드
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

                // CustomTabs으로 웹 브라우저 열기

                // 1. CustomTabsServiceConnection 지원 브라우저 열기
                // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
                try {
                    KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
                } catch(e: UnsupportedOperationException) {
                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                }

                // 2. CustomTabsServiceConnection 미지원 브라우저 열기
                // ex) 다음, 네이버 등
                try {
                    KakaoCustomTabsClient.open(requireContext(), sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                }
            }
        }
    }




}