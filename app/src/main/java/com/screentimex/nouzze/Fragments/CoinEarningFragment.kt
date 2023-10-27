package com.screentimex.nouzze.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.screentimex.nouzze.Firebase.FireBaseFragments
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.FragmentCoinEarningBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails

class CoinEarningFragment : Fragment() {
    private lateinit var coinEarningBinding: FragmentCoinEarningBinding
    private lateinit var mSharedPrefFreePoints: SharedPreferences
    private lateinit var mUserDetails: UserDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        coinEarningBinding = FragmentCoinEarningBinding.inflate(inflater, container, false)

        mSharedPrefFreePoints = requireContext().getSharedPreferences(Constants.FREE_POINTS, Context.MODE_PRIVATE)

        if (!mSharedPrefFreePoints.getBoolean(Constants.FREE_POINTS_BOOL, false)) {
            val editorFreePoints = mSharedPrefFreePoints.edit()
            editorFreePoints.putBoolean(Constants.FREE_POINTS_BOOL, true)
            editorFreePoints.putInt(Constants.FREE_POINTS, 50)
            editorFreePoints.apply()
            editorFreePoints.commit()
        }

        if(isInternetConnected(requireContext())) {
            FireBaseFragments().loadUserData(this)
        } else {
            showSnackBar("No Internet !!")
        }

        coinEarningBinding.EarnCoinsCardView.setOnClickListener{
            getFreePoints()
        }

        return coinEarningBinding.root
    }

    fun loadUserDataSuccessfully(user: UserDetails) {
        mUserDetails = user
    }

    fun failedToGetUserData(message: String) {
        showSnackBar(message)
    }
    private fun getFreePoints() {
        if(mSharedPrefFreePoints.contains(Constants.FREE_POINTS)) {
            val pointsHashMap = HashMap<String, Any>()
            pointsHashMap[Constants.POINTS] = mSharedPrefFreePoints.getInt(Constants.FREE_POINTS, 0) + mUserDetails.points
            val editorFreePoints = mSharedPrefFreePoints.edit()
            editorFreePoints.remove(Constants.FREE_POINTS)
            editorFreePoints.apply()
            editorFreePoints.commit()
            showToast("Claimed Successfully")
            FireBaseFragments().updateProfileData(this,pointsHashMap)
        } else {
            showToast("Already claimed, come back tomorrow")
        }
    }
    fun showToast(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    fun showSnackBar(message: String) {
        view?.let { fragmentView ->
            val snackBar = Snackbar.make(fragmentView, message, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.snackBarColor
                )
            )
            snackBar.show()
        }
    }

}