package com.screentimex.nouzze.Activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.auth.User
import com.screentimex.nouzze.Adapters.LeaderBoardAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityLeaderBoardBinding
import com.screentimex.nouzze.models.LeaderBoardDetails
import com.screentimex.nouzze.models.UserDetails

class LeaderBoard : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        binding.progressBarButton.visibility = View.VISIBLE
        FireStoreClass().getAllUserData(this@LeaderBoard)
    }

    fun getLeaderBoardData(data: ArrayList<LeaderBoardDetails>) {
        val topTenUsers = arrayListOf<LeaderBoardDetails>()
        data.sortByDescending { it.userPoints }
        for(i in 0..<minOf(data.size, 10)) {
            data[i].userRank = i + 1
            topTenUsers.add(data[i])
        }
        val adapter = LeaderBoardAdapter(this@LeaderBoard, topTenUsers)
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(this@LeaderBoard)
        binding.leaderboardRecyclerView.adapter = adapter
        binding.progressBarButton.visibility = View.GONE
    }

    fun failedToGetData(message: String) {
        Toast.makeText(this@LeaderBoard, message, Toast.LENGTH_SHORT).show()
        binding.progressBarButton.visibility = View.GONE
    }
    private fun setUpActionBar() {
        setSupportActionBar(binding.customToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Leaderboard"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        binding.customToolBar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            onBackPressed()
        }
    }
}