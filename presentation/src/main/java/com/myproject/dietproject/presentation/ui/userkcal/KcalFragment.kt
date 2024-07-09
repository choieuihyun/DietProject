package com.myproject.dietproject.presentation.ui.userkcal

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFragmentBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import com.myproject.dietproject.presentation.ui.LoadingProgress
import com.myproject.dietproject.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalFragment : BaseFragment<KcalFragmentBinding>(R.layout.kcal_fragment) {

    private val viewModel: KcalViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var dialog: Dialog
    private val args by navArgs<KcalFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity

        Log.d("onAttachKcal", "onAttachKcal")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity.getBinding.bottomNavigationView.isVisible = false
        viewModel.userId = args.userId // 이렇게 할 바에 그냥 전역으로 두고말지

        dialog = LoadingProgress(requireContext())

        Log.d("onCreateKcal", "onCreateKcal")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("onCreateViewKcal", "onCreateViewKcal")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreatedKcal", "onViewCreatedKcal")

        //setupRecyclerView()

        val viewPagerAdapter = KcalFragmentViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "검색"
                1 -> tab.text = "즐겨찾기"
            }
        }.attach()

        setupEdittext()

        setupUI()


    }

    override fun onStart() {
        super.onStart()
        Log.d("onStartKcal", "onStartKcal")
    }

    override fun onResume() {
        super.onResume()

        Log.d("onResumeKcal", "onResumeKcal")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPauseKcal", "onPauseKcal")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyViewKcal", "onDestroyViewKcal")
    }

    private fun setupTabLayout() {



    }

    private fun setupUI() {

        binding.searchButton.setOnClickListener {

            val searchText = binding.appCompatEditText.text.toString()

            viewModel.getKcalData(searchText)

            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) dialog.show()
                else dialog.dismiss()
            }
        }

    }

    private fun setupRecyclerView() {

//        kcalListAdapter = KcalAdapter()
//
//        binding.addUserKcalRecyclerView.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            adapter = kcalListAdapter
//        }
//
//        kcalListAdapter.setOnItemClickListener {
//            kcal ->
//            val action = KcalFragmentDirections.actionKcalFragmentToKcalDetailFragment(args.userId, kcal)
//            findNavController().navigate(action)
//        }

    }

    private fun setupEdittext() {

        binding.appCompatEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }


}