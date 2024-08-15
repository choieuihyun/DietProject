package com.myproject.dietproject.presentation.ui.userkcal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFavoriteListItemBinding
import com.myproject.dietproject.presentation.databinding.KcalFavoriteListItemTypeTwoBinding


class KcalFragmentFavoriteListAdapter : ListAdapter<FoodDiaryModel, ViewHolder>(kcalDataDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            KcalFragmentFavoriteListAdapter.type_one -> {

                KcalFavoriteViewHolder(
                    KcalFavoriteListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )

            }

            KcalFragmentFavoriteListAdapter.type_two -> {

                KcalFavoriteViewHolderTypeTwo(
                    KcalFavoriteListItemTypeTwoBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                KcalFavoriteViewHolderTypeTwo(
                    KcalFavoriteListItemTypeTwoBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = currentList[position]

        when (holder) {

            is KcalFavoriteViewHolder -> {
                holder.bind(item)
                /**
                 * 여기다 리스너 끼운 이유?
                 * viewHolder 각각마다 클릭 리스너를 가질 것이고, viewHolder의 클릭 동작에 대한 책임은 Adapter가 들고있어야 한다고 판단.
                 * Fragment에서 viewHolder에 대한 참조를 갖진 않기 때문도 있다고 판단.
                 * 근데 그러면 SharedPreference 어떻게 쓸꺼?
                 * 근데 이렇게하면 뷰홀더에 리스너 만드는 구조랑 뭐가 다를까..
                 */

                setFavoriteButtonClickListener(holder.imageButton, item)
            }

            is KcalFavoriteViewHolderTypeTwo -> {
                holder.bind(item)

//                holder.imageButton.setOnClickListener { imageButton ->
//                    setFavoriteButtonClickListener(imageButton, item as Kcal)
//                }
                setFavoriteButtonClickListener(holder.imageButton, item)
            }

        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(item)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].makerName != "")
            KcalFragmentListAdapter.type_one
        else
            KcalFragmentListAdapter.type_two
    }

    private var onItemClickListener: ((FoodDiaryModel) -> Unit)? = null
    private var onFavoriteButtonClickListener: ((FoodDiaryModel, View) -> Unit)? = null

    fun setOnItemClickListener(listener: (FoodDiaryModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnFavoriteButtonClickListener(listener: (FoodDiaryModel, View) -> Unit) {
        onFavoriteButtonClickListener = listener
    }

    private fun setFavoriteButtonClickListener(imageButton: View, item: FoodDiaryModel) {
        imageButton.setOnClickListener {
            val isSelected = it.isSelected
            it.isSelected = !isSelected

            // 아니 이런게 가능했다고?
            it.setBackgroundResource(
                if (isSelected)
                    R.drawable.star_border
                else
                    R.drawable.star_filled
            )
            onFavoriteButtonClickListener?.invoke(item, it)
        }
    }

    companion object {

        const val type_one = 1
        const val type_two = 2

        private val kcalDataDiffCallback = object : DiffUtil.ItemCallback<FoodDiaryModel>() {

            override fun areItemsTheSame(oldItem: FoodDiaryModel, newItem: FoodDiaryModel): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: FoodDiaryModel, newItem: FoodDiaryModel): Boolean {
                return oldItem == newItem
            }


        }

    }



}