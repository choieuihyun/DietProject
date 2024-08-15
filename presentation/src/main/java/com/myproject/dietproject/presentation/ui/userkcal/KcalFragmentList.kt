package com.myproject.dietproject.presentation.ui.userkcal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFragmentListBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KcalFragmentList : BaseFragment<KcalFragmentListBinding>(R.layout.kcal_fragment_list) {

    private lateinit var recyclerViewAdapter: KcalFragmentListAdapter
    private val viewModel: KcalViewModel by activityViewModels()
    private lateinit var userId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("onAttachKcalList", "onAttachKcalList")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreateKcalList", "onCreateKcalList")

        userId = viewModel.userId
        viewModel.setSharedPreference("favoriteButtonPreference")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("onCreateViewKcalList", "onCreateViewKcalList")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreatedKcalList", "onViewCreatedKcalList")
        setupRecyclerView()

        viewModel.getEmptyKcalData()

        viewModel.kcalData.observe(viewLifecycleOwner) {

            if (it != null) {
                recyclerViewAdapter.submitList(it)
                for(i in it) {
                    if(i.favoriteButtonState)
                        Log.d("kcal123", i.dESCKOR.toString())
                }
            } else
                binding.kcalRecyclerView.visibility = View.GONE

        }

        recyclerViewAdapter.setOnItemClickListener { kcal ->
            val action = KcalFragmentDirections.actionKcalFragmentToKcalDetailFragment(userId,null , kcal)
            findNavController().navigate(action)
        }

        recyclerViewAdapter.setOnFavoriteButtonClickListener { kcal, imageButton ->

            val foodName = kcal.dESCKOR

            if(imageButton.isSelected) {
                // 개인적으로 이런 구조도 view에 Model에 관한 코드가 있어서 올바른건가 싶긴 하다.
                val foodDiary = FoodDiaryModel(
                    kcal = kcal.nUTRCONT1,
                    foodName = kcal.dESCKOR,
                    makerName = kcal.mAKERNAME,
                    carbonHydrate = kcal.nUTRCONT2,
                    protein = kcal.nUTRCONT3,
                    fat = kcal.nUTRCONT4,
                    favoriteButtonState = true
                )
                viewModel.addFoodDiary(foodDiary)
                viewModel.addSharedPreferenceFavoriteState(foodName!!, foodName)
            }
            else {

                // 이것도 이름으로 해도 될거같은데?
                val foodDiary = FoodDiaryModel(
                    kcal = kcal.nUTRCONT1,
                    foodName = kcal.dESCKOR,
                    makerName = kcal.mAKERNAME,
                    carbonHydrate = kcal.nUTRCONT2,
                    protein = kcal.nUTRCONT3,
                    fat = kcal.nUTRCONT4,
                    favoriteButtonState = false
                )
                viewModel.deleteSharedPreferenceFavoriteState(foodName!!)
                viewModel.deleteFoodDiary(foodDiary.foodName!!)
            }

        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("onStartKcalList", "onStartKcalList")
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateKcalListFavoriteState()

        Log.d("onResumeKcalList", "onResumeKcalList")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPauseKcalList", "onPauseKcalList")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyViewKcalList", "onDestroyViewKcalList")
    }

    private fun setupRecyclerView() {

        recyclerViewAdapter = KcalFragmentListAdapter()

        binding.kcalRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }

    }




}