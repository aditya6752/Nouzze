package com.screentimex.nouzze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.LeaderBoardListBinding
import com.screentimex.nouzze.models.LeaderBoardDetails

class LeaderBoardAdapter(private val context: Context, private val list: ArrayList<LeaderBoardDetails>):
RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LeaderBoardListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            holder.userRank.text = model.userRank.toString()
            Glide.with(context)
                .load(model.userImage)
                .centerCrop()
                .placeholder(R.drawable.ic_user_holder)
                .into(holder.userImage)
            holder.userName.text = model.userName
            holder.userPoints.text = model.userPoints.toString()
        }
    }

    private fun formatNumber(number: Long): String {
        return when {
            number in 1000..999999 -> {
                val thousands = number / 1000
                "${thousands}k"
            }
            number >= 1000000 -> {
                val millions = number / 1000000
                "${millions}M"
            }
            else -> number.toString()
        }
    }

    class MyViewHolder(binding: LeaderBoardListBinding): RecyclerView.ViewHolder(binding.root) {
        val userRank = binding.userRank
        val userImage = binding.userImage
        val userName = binding.userName
        val userPoints = binding.userPoints
    }
}