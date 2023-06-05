package com.myproject.dietproject.presentation.ui.userkcal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.myproject.dietproject.domain.model.Kcal
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myproject.dietproject.presentation.databinding.KcalListItemBinding
import com.myproject.dietproject.presentation.databinding.KcalListItemTypeTwoBinding


class KcalAdapter : ListAdapter<Kcal, ViewHolder>(kcalDataDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when(viewType) {
            type_one -> {
                KcalViewHolder(KcalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            type_two -> {
                KcalViewHolderTypeTwo(KcalListItemTypeTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                KcalViewHolderTypeTwo(KcalListItemTypeTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userKcal = currentList[position]

        when(holder) {

            is KcalViewHolder -> holder.bind(userKcal)

            is KcalViewHolderTypeTwo -> holder.bind(userKcal)

        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(userKcal)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position].mAKERNAME != "")
            type_one
        else
            type_two
    }

    private var onItemClickListener: ((Kcal) -> Unit)? = null

    fun setOnItemClickListener(listener: (Kcal) -> Unit) {
        onItemClickListener = listener
    }


    companion object {

        const val type_one = 1
        const val type_two = 2

        private val kcalDataDiffCallback = object : DiffUtil.ItemCallback<Kcal>() {

            override fun areItemsTheSame(oldItem: Kcal, newItem: Kcal): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Kcal, newItem: Kcal): Boolean {
                return oldItem == newItem
            }


        }

    }



}