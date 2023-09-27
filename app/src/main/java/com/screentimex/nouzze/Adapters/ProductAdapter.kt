package com.screentimex.nouzze.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.screentimex.nouzze.Activities.Address
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.PreferenceManager
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails

class ProductAdapter (val productDetailsList : ArrayList<ProductDetails>, var context : Activity  ) : RecyclerView.Adapter<ProductAdapter.ProductViewModel>() {
    // shared preference
    private val preferenceManagerProductAdapter = PreferenceManager(context)
    inner class ProductViewModel(itemView : View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById<TextView>(R.id.productName)
        val productImg = itemView.findViewById<ImageView>(R.id.productImg)
        val productPrice = itemView.findViewById<TextView>(R.id.productPrice)
        val productButton = itemView.findViewById<Button>(R.id.productButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.products,parent,false)
        return ProductViewModel(itemView)
    }

    override fun getItemCount(): Int {
        return productDetailsList.count()
    }

    override fun onBindViewHolder(holder: ProductViewModel, position: Int) {
        var currentItem : ProductDetails = productDetailsList[position]
        holder.productName.text = currentItem.productName
        val imgId = context.resources.getIdentifier(currentItem.productImg,"drawable",context.packageName)
        holder.productImg.setImageResource(imgId)
        holder.productPrice.text = currentItem.productPrice
        holder.productButton.setOnClickListener {
            var intent = Intent(context, Address::class.java)
            preferenceManagerProductAdapter.saveDataObject(Constants.PRODUCTDETAILS, currentItem)
            context.startActivityForResult(intent, 101)
        }
    }
}