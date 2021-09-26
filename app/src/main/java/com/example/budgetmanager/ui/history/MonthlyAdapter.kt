package com.example.budgetmanager.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.database.models.MonthlyResult
import com.example.budgetmanager.databinding.MonthlyCardLayoutBinding

class MonthlyAdapter(private val clickListener: MonthlyClickListener) : ListAdapter<MonthlyResult, MonthlyViewHolder>(MonthlyDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyViewHolder {
        return MonthlyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MonthlyViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class MonthlyViewHolder(val binding: MonthlyCardLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): MonthlyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = MonthlyCardLayoutBinding.inflate(layoutInflater, parent, false)
            return MonthlyViewHolder(binding)
        }
    }
    fun bind(monthlyResult: MonthlyResult, clickListener: MonthlyClickListener) {
        binding.monthlyResult = monthlyResult
        binding.root.setOnClickListener {
            clickListener.onMonthlyClick(monthlyResult)
        }
    }
}

interface MonthlyClickListener {
    fun onMonthlyClick(monthlyResult: MonthlyResult)
}

class MonthlyDiffCallback :
    DiffUtil.ItemCallback<MonthlyResult>() {
    override fun areItemsTheSame(oldItem: MonthlyResult, newItem: MonthlyResult): Boolean {
        return oldItem.dateLong == newItem.dateLong
    }

    override fun areContentsTheSame(oldItem: MonthlyResult, newItem: MonthlyResult): Boolean {
        return oldItem == newItem
    }
}