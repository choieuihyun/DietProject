package com.myproject.dietproject.presentation.ui.userkcal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFragmentFavoriteListBinding
import com.myproject.dietproject.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KcalFragmentFavoriteList: BaseFragment<KcalFragmentFavoriteListBinding>(R.layout.kcal_fragment_favorite_list) {

    // 뷰 하나에 recyclerView로 갈아서 끼우는게 아니라 Fragment를 두개 만든이유?
    // 즐겨찾기는 RoomDB를 사용해야한다고 생각했고, 이 과정에서 클릭이벤트 같은 것과 함께 즐겨찾기 기능 등을 만들어야 하기에 기능 구현할 것이 많다 생각해 프래그먼트 두개로 만듦.
    // 그런데 args가 문제네?
    // 근데 이 args를 이렇게 데이터로 옮겨다니는 것보다 그냥 homeFragment 시작점에서 전역으로 초기화하게 두면 안되나?
    private lateinit var recyclerViewAdapter: KcalFragmentFavoriteListAdapter
    private val viewModel: KcalViewModel by activityViewModels()
    private lateinit var userId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("onAttachFaKcalList", "onAttachFaKcalList")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreateFaKcalList", "onCreateFaKcalList")

        userId = viewModel.userId

        viewModel.setSharedPreference("favoriteButtonPreference")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateViewFaKcalList", "onCreateViewFaKcalList")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreatedFaKcalList", "onViewCreatedFaKcalList")

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowTest.collect { favoriteList ->
                    recyclerViewAdapter.submitList(favoriteList)
                    Log.d("sdfsdf23",favoriteList.toString())
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("onStartFaKcal", "onStartFaKcal")
    }

    override fun onResume() {
        super.onResume()

//        viewModel.getFavoriteList()
//
//        viewModel.favoriteList.observe(viewLifecycleOwner) {
//
//            recyclerViewAdapter.submitList(it)
//
//        }


        recyclerViewAdapter.setOnItemClickListener { foodDiaryModel ->
            val action = KcalFragmentDirections.actionKcalFragmentToKcalDetailFragment(
                userId,
                foodDiaryModel,
                null
            )
            findNavController().navigate(action)
        }

        // 검색 리스트 나갔다오면 즐겨찾기 버튼 유지 안됨, 버튼 클릭에 따라 즐겨찾기 리스트 갱신 안됨.
/*        recyclerViewAdapter.setOnFavoriteButtonClickListener { foodDiaryModel, imageButton ->
            val foodName = foodDiaryModel.foodName
            val newState = !imageButton.isSelected

            if (newState) {
                viewModel.addFavoriteItem(foodDiaryModel.copy(favoriteButtonState = true))
            } else {
                viewModel.removeFavoriteItem(foodName!!)
            }
        }*/

        recyclerViewAdapter.setOnFavoriteButtonClickListener { foodDiaryModel, imageButton ->

            val foodName = foodDiaryModel.foodName

            if(imageButton.isSelected) {

                val foodDiary = FoodDiaryModel(
                    kcal = foodDiaryModel.kcal,
                    foodName = foodDiaryModel.foodName,
                    makerName = foodDiaryModel.makerName,
                    favoriteButtonState = true
                )
                viewModel.addFoodDiary(foodDiary)
                viewModel.addSharedPreferenceFavoriteState(foodName!!, foodName)
                //viewModel.resetFavoriteList()
            }
            else {

                // 이름으로 해도 될거같은데?
                val foodDiary = FoodDiaryModel(
                    kcal = foodDiaryModel.kcal,
                    foodName = foodDiaryModel.foodName,
                    makerName = foodDiaryModel.makerName,
                    favoriteButtonState = false
                )
                Log.d("foodDiary", foodDiary.toString())

                viewModel.deleteSharedPreferenceFavoriteState(foodName!!)
                viewModel.deleteFoodDiary(foodDiary.foodName!!)
                //viewModel.getFavoriteList()

            }

            //viewModel.getFavoriteList()
        }


        Log.d("onResumeFaKcal", "onResumeFaKcal")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPauseFaKcal", "onPauseFaKcal")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyViewFaKcal", "onDestroyViewFaKcal")
    }

    private fun setupRecyclerView() {

        recyclerViewAdapter = KcalFragmentFavoriteListAdapter()

        binding.favoriteKcalRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }

    }


}