package com.screentimex.nouzze.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.screentimex.nouzze.models.AddressDetails
import com.screentimex.nouzze.R

class AddressAdapter(var addressDetailsList : ArrayList<AddressDetails>, val context : Activity) : RecyclerView.Adapter<AddressAdapter.AddressViewModel>() {
    inner class AddressViewModel (itemView  : View) : RecyclerView.ViewHolder(itemView) {
        var Name = itemView.findViewById<TextView>(R.id.User_Name)
        var Area = itemView.findViewById<TextView>(R.id.User_Area_FlatNumber)
        var City = itemView.findViewById<TextView>(R.id.User_City_State_Pin)
        var Mobile_Number = itemView.findViewById<TextView>(R.id.User_Mobile_Number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.address_card_view,parent,false)
        return AddressViewModel(itemView)
    }

    override fun getItemCount(): Int {
        return addressDetailsList.count()
    }

    override fun onBindViewHolder(holder: AddressViewModel, position: Int) {
        var current_Item = addressDetailsList[position]
        holder.Name.text = current_Item.Name
        holder.Area.text = "${current_Item.Flat_Number} / ${current_Item.Area} /${current_Item.Landmark}"
        holder.City.text = "${current_Item.City} / ${current_Item.State} / ${current_Item.Pincode}"
        holder.Mobile_Number.text = current_Item.Mobile_Number
    }
}