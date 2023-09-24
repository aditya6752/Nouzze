package com.screentimex.nouzze.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.screentimex.nouzze.R
import com.screentimex.nouzze.models.Category

class CategoryAdapter(val categoryList : List<Category>, val context : Activity  ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewModel>() {

    private lateinit var myListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setItemClickListener(listener: onItemClickListener){
        myListener = listener
    }

    inner class CategoryViewModel(itemView : View , listener : onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val catName = itemView.findViewById<TextView>(R.id.categoryName)
        val catImg = itemView.findViewById<ImageView>(R.id.categoryImg)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categories,parent,false)
        return CategoryViewModel(itemView,myListener)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewModel, position: Int) {
        var currentItem = categoryList[position]
        holder.catName.text = currentItem.categoryName
        val imgId = context.resources.getIdentifier(currentItem.categoryimg,"drawable",context.packageName)
        holder.catImg.setImageResource(imgId)
    }

    override fun getItemCount(): Int {
        return categoryList.count()
    }
}