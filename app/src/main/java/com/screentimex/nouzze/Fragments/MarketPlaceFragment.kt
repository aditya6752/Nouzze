package com.screentimex.nouzze.Fragments

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Services.DataService
import com.screentimex.nouzze.Activities.Products
import com.screentimex.nouzze.Adapters.CategoryAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.FragmentMarketPlaceBinding
import com.screentimex.nouzze.models.Category
import com.screentimex.nouzze.models.Constants

class MarketPlaceFragment : Fragment() {
    private lateinit var marketPlaceBinding: FragmentMarketPlaceBinding
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var  categoriesList :ArrayList<Category>
    lateinit var categoryName : String
    lateinit var  categoryAdapter : CategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        marketPlaceBinding = FragmentMarketPlaceBinding.inflate(inflater, container, false)
        marketPlaceBinding.marketPlaceIntroText.text =
            stringToBoldItalic("Hi, This is Market Place, You Can Redeem Your Coins Here")
        categoryRecyclerView = marketPlaceBinding.RecylerViewCategories
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        categoryAdapter = CategoryAdapter(DataService.categories,requireContext())
        categoryRecyclerView.adapter = categoryAdapter

        categoryAdapter.setItemClickListener( object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), Products::class.java)
                val productName = DataService.categories[position].categoryName
                intent.putExtra(Constants.PRODUCT_NAME, productName)
                startActivity(intent)
            }
        })

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
}