package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RowItemBinding
import com.example.myapplication.model.data.Lfs

class ListAdapter : RecyclerView.Adapter<ListAdapter.MainViewHolder>() {
    private lateinit var binding: RowItemBinding
    var items = mutableListOf<Lfs>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = RowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        binding.acronym.text = items[position].lf
    }

    override fun getItemCount(): Int = items.size

    fun setData(items: List<Lfs>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}