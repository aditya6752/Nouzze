package com.screentimex.nouzze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.CategoriesBinding
import com.screentimex.nouzze.models.CategoryModel

class CategoryAdapter(private val context: Context, private val list: ArrayList<CategoryModel>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(CategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            Glide.with(context)
                .load(model.img)
                .centerCrop()
                .placeholder(R.drawable.ic_user_holder)
                .into(holder.image)
            holder.name.text = model.cat

            holder.itemView.setOnClickListener {
                if(onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    interface OnClickListener{
        fun onClick(position: Int, model: CategoryModel)
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }


    class MyViewHolder(private val binding: CategoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.categoryImg
        val name = binding.categoryName
    }

}