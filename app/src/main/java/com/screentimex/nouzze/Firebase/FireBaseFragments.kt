package com.screentimex.nouzze.Firebase

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.Fragments.CoinEarningFragment
import com.screentimex.nouzze.Fragments.LeaderboardFragment
import com.screentimex.nouzze.Fragments.ScreenTimeFragment
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.LeaderBoardDetails
import com.screentimex.nouzze.models.UserDetails

class FireBaseFragments: AppCompatActivity() {
    private val mFireStore = FirebaseFirestore.getInstance()
    fun getAllUserData(fragment: Fragment) {
        mFireStore.collection(Constants.USERS).get().addOnSuccessListener { doc ->
            val leaderBoardList = ArrayList<LeaderBoardDetails>()
            for (it in doc) {
                val data = it.toObject(UserDetails::class.java)
                leaderBoardList.add(LeaderBoardDetails(
                    0, data.image, data.name, data.points
                ))
            }
            when (fragment) {
                is LeaderboardFragment -> {
                    fragment.getLeaderBoardData(leaderBoardList)
                }
            }
        }.addOnFailureListener {
            when (fragment) {
                is LeaderboardFragment -> {
                    fragment.failedToGetData(it.message!!)
                }
            }
        }
    }

    fun loadUserData(fragment: Fragment) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(UserDetails::class.java)
                if(user != null) {
                    when(fragment) {
                        is ScreenTimeFragment -> {
                           fragment.loadUserDataSuccessfully(user)
                        }
                        is CoinEarningFragment ->{
                            fragment.loadUserDataSuccessfully(user)
                        }
                    }
                }
            }.addOnFailureListener {
                when(fragment) {
                    is ScreenTimeFragment -> {
                        fragment.failedToGetUserData(it.message!!)
                    }
                }
            }
    }
    fun updateProfileData(fragment: Fragment,
                          userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                if(fragment is CoinEarningFragment) {
                    FireBaseFragments().loadUserData(fragment)
                }
            }.addOnFailureListener{
                if(fragment is CoinEarningFragment) {
                    fragment.showToast(it.message!!)
                }
                Log.e("FireStoreClassSignUp", it.message!!)
            }
    }
    private fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}