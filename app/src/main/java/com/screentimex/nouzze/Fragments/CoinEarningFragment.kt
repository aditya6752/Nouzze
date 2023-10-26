package com.screentimex.nouzze.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.FragmentCoinEarningBinding

class CoinEarningFragment : Fragment() {
    private lateinit var coinEarningBinding: FragmentCoinEarningBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        coinEarningBinding = FragmentCoinEarningBinding.inflate(inflater, container, false)

        return coinEarningBinding.root
    }


}