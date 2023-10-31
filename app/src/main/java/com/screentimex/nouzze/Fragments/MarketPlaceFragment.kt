package com.screentimex.nouzze.Fragments

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.screentimex.nouzze.Activities.Products
import com.screentimex.nouzze.Adapters.CategoryAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.FragmentMarketPlaceBinding
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.CategoryModel
import com.screentimex.nouzze.models.Constants

class MarketPlaceFragment : Fragment() {
    private lateinit var marketPlaceBinding: FragmentMarketPlaceBinding
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var  categoriesList :ArrayList<Category>
    lateinit var categoryName : String
    lateinit var categoryAdapter : CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        marketPlaceBinding = FragmentMarketPlaceBinding.inflate(inflater, container, false)
        marketPlaceBinding.marketPlaceIntroText.text =
            stringToBoldItalic("Hi, This is Market Place, You Can Redeem Your Coins Here")
        getCategories()

        return marketPlaceBinding.root
    }

    private fun stringToBoldItalic(str: String): Spannable{
        val spannable = SpannableStringBuilder(str)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(boldSpan, 0, spannable.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val italicSpan = StyleSpan(Typeface.ITALIC)
        spannable.setSpan(italicSpan, 0, spannable.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannable
    }

    private fun getCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                Log.i("SetUp","Going")
                setUpRecyclerView(list)
//                marketPlaceBinding.RecylerViewCategories.adapter = CategoryAdapter(list ,requireContext() )
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Something went wrong !!",Toast.LENGTH_SHORT).show()
            }
    }
    private fun setUpRecyclerView( list : ArrayList<CategoryModel>){
        categoryRecyclerView = marketPlaceBinding.RecylerViewCategories

        categoryAdapter = CategoryAdapter(requireContext(),list)
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryRecyclerView.adapter = categoryAdapter

        categoryAdapter.setItemClickListener( object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), Products::class.java)
                val productName = DataService.categories[position].categoryName
                intent.putExtra(Constants.PRODUCT_NAME, productName)
                startActivity(intent)
            }
        })

    }
}