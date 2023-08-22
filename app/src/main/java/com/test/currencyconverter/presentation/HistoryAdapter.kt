package com.test.currencyconverter.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.currencyconverter.databinding.HistoricalItemLayoutBinding
import com.test.currencyconverter.models.HistoricalItem

class HistoryAdapter(private val currencyList: List<HistoricalItem>) : RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = HistoricalItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = currencyList[position]
        holder.bindItem(currentItem)
    }

    override fun getItemCount() = currencyList.size

    inner class ItemViewHolder(private val binding: HistoricalItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: HistoricalItem) {
            binding.item = item;
        }
    }
}
