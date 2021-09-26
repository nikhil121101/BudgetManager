package com.example.budgetmanager.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.databinding.TokenCardLayoutBinding

class TokenAdapter(private val clickListener: ClickListener) : ListAdapter<Token, TokenViewHolder>(TokenDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokenViewHolder {
        return TokenViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TokenViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class TokenViewHolder(val binding: TokenCardLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): TokenViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TokenCardLayoutBinding.inflate(layoutInflater, parent, false)
            return TokenViewHolder(binding)
        }
    }
    fun bind(token_arg: Token, clickListener: ClickListener) {
        binding.token = token_arg
        binding.root.setOnClickListener {
            clickListener.onTokenClick(token_arg)
        }
    }
}

interface ClickListener {
    fun onTokenClick(token: Token)
}

class TokenDiffCallback :
    DiffUtil.ItemCallback<Token>() {
    override fun areItemsTheSame(oldItem: Token, newItem: Token): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Token, newItem: Token): Boolean {
        return oldItem == newItem
    }
}