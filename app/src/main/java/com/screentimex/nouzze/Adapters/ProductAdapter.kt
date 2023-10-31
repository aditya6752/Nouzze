package com.screentimex.nouzze.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.screentimex.nouzze.Activities.Address
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.ProductDetailSharedPref
import com.screentimex.nouzze.databinding.CategoriesBinding
import com.screentimex.nouzze.databinding.ProductsBinding
import com.screentimex.nouzze.models.CategoryModel
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails

class ProductAdapter (val productDetailsList : ArrayList<ProductDetails>, var context : Activity)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // shared preference
    private val productDetailSharedPrefProductAdapter = ProductDetailSharedPref(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount(): Int {
        return productDetailsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = productDetailsList[position]
        if(holder is MyViewHolder) {
            Glide.with(context)
                .load(model.productCoverImage)
                .placeholder(R.drawable.ic_user_holder)
                .into(holder.productImage)
            holder.productName.text = model.productName
            holder.productPrice.text = model.productMrp

            holder.productButton.setOnClickListener {
                var intent = Intent(context, Address::class.java)
                productDetailSharedPrefProductAdapter.saveDataObject(Constants.PRODUCTDETAILS, model)
                context.startActivityForResult(intent, 101)
            }
        }
    }

    class MyViewHolder(private val binding: ProductsBinding) : RecyclerView.ViewHolder(binding.root) {
        val productName = binding.productName
        val productImage = binding.productImg
        val productPrice = binding.productPrice
        val productButton = binding.productButton
    }
}