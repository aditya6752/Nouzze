package com.screentimex.nouzze.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.CategoriesBinding
import com.screentimex.nouzze.databinding.LeaderBoardListBinding
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.CategoryModel
import com.screentimex.nouzze.models.LeaderBoardDetails

class CategoryAdapter(private val context: Context, private val list: ArrayList<CategoryModel>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(CategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

    inner class MyViewHolder(private val binding: LeaderBoardListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: LeaderBoardDetails) {
            binding.userRank.text = model.userRank.toString()
            Glide.with(context)
                .load(model.userImage)
                .centerCrop()
                .placeholder(R.drawable.ic_user_holder)
                .into(binding.userImage)
            binding.userName.text = model.userName
            binding.userPoints.text = model.userPoints.toString()

            // Set the click listener for the item
            binding.root.setOnClickListener {
                onClickListener(model)
            }
        }
    }

}