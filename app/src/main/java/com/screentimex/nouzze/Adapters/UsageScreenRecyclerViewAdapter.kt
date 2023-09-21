package com.screentimex.nouzze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ListItemScreenUsageRecyclerViewBinding
import com.screentimex.nouzze.models.ScreenUsageData

class UsageScreenRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<ScreenUsageData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ListItemScreenUsageRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            Glide.with(context)
                .load(model.appIconImage)
                .centerCrop()
                .placeholder(R.drawable.appicon)
                .into(holder.appIconImage)
            holder.appName.text = model.appName
            holder.usageTime.text = model.appUsagetime
        }
    }

    class MyViewHolder(binding: ListItemScreenUsageRecyclerViewBinding): RecyclerView.ViewHolder(binding.root) {
        val appIconImage = binding.appIconImage
        val appName = binding.appName
        val usageTime = binding.usageTime
    }
}