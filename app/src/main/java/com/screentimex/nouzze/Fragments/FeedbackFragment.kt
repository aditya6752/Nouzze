package com.screentimex.nouzze.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.screentimex.nouzze.Firebase.FireBaseFragments
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.FragmentFeedbackBinding
import com.screentimex.nouzze.databinding.FragmentMarketPlaceBinding

class FeedbackFragment : Fragment() {

    private lateinit var binding : FragmentFeedbackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        FireBaseFragments().getAllUserData(this)

        //binding.positiveFeedbackText.text = positiveText()
        //binding.negativeFeedbackText.text = negativeText()

        return binding.root
    }

    private fun negativeText(): String {
        val text = "Don't worry, we all have our distractions. Here are the bottom three apps where you've spent more time than desired:\n[List the names of the apps]\nIt's okay, we can work on this together! \uD83D\uDCAA"
        return text
    }

    private fun positiveText(): String {
        val text = "Congratulations! \uD83D\uDE80 You're acing your productivity game. Here are your top three most productive apps: \n[List the names of the apps] \nKeep up the good work! \uD83C\uDF1F"
        return text
    }
}