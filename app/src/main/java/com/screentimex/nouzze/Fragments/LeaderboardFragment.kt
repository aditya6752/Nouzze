package com.screentimex.nouzze.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.screentimex.nouzze.Adapters.LeaderBoardAdapter
import com.screentimex.nouzze.Firebase.FireBaseFragments
import com.screentimex.nouzze.databinding.FragmentLeaderboardBinding
import com.screentimex.nouzze.models.LeaderBoardDetails

class LeaderboardFragment : Fragment() {
    private lateinit var leaderBoardBinding: FragmentLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        leaderBoardBinding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        // Pass 'this' fragment as an argument to getAllUserData
        leaderBoardBinding.progressBarButton.visibility = View.VISIBLE
        if(leaderBoardBinding.leaderboardRecyclerView.adapter == null) {
            FireBaseFragments().getAllUserData(this)
        }

        return leaderBoardBinding.root
    }

    fun getLeaderBoardData(data: ArrayList<LeaderBoardDetails>) {
        val topTenUsers = arrayListOf<LeaderBoardDetails>()
        data.sortByDescending { it.userPoints }
        for (i in 0 until minOf(data.size, 10)) {
            data[i].userRank = i + 1
            topTenUsers.add(data[i])
        }
        val adapter = LeaderBoardAdapter(requireContext(), topTenUsers)
        leaderBoardBinding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        leaderBoardBinding.leaderboardRecyclerView.adapter = adapter
        leaderBoardBinding.progressBarButton.visibility = View.GONE
    }

    fun failedToGetData(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
