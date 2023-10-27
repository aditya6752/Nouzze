package com.screentimex.nouzze.Activities

import android.app.Activity
import android.app.AppOpsManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.screentimex.nouzze.Adapters.AppInfoListAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.Firebase.SignInActivity
import com.screentimex.nouzze.Fragments.CoinEarningFragment
import com.screentimex.nouzze.Fragments.FeedbackFragment
import com.screentimex.nouzze.Fragments.LeaderboardFragment
import com.screentimex.nouzze.Fragments.MarketPlaceFragment
import com.screentimex.nouzze.Fragments.ScreenTimeFragment
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.MidNightUsageStateSharedPref
import com.screentimex.nouzze.Services.MidNightWordManager
import com.screentimex.nouzze.Services.PointsCalculation
import com.screentimex.nouzze.UsageStats.Utils
import com.screentimex.nouzze.databinding.ActivityDrawerBinding
import com.screentimex.nouzze.models.AppInfo
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var mUser: FirebaseUser
    private lateinit var mUserDetails: UserDetails
    private lateinit var mSharedPreferences: SharedPreferences

    private lateinit var mSharedPrefMidNightUserDetails: MidNightUsageStateSharedPref
    private lateinit var mSharedPrefPointsStoreMidNight: SharedPreferences
    private lateinit var mSharedPrefAboutAppDialog: SharedPreferences
    private lateinit var mSharedPrefFreePoints: SharedPreferences

    var i = 0
    companion object {
        const val MY_PROFILE_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        // email verification
        mUser = FirebaseAuth.getInstance().currentUser!!

        mSharedPreferences =
            getSharedPreferences(Constants.USAGE_PERMISSION_SHARED_PREFS, Context.MODE_PRIVATE)
        FireStoreClass().loadUserData(this@MainActivity)
        mSharedPrefMidNightUserDetails = MidNightUsageStateSharedPref(this@MainActivity)

        mSharedPrefPointsStoreMidNight =
            getSharedPreferences(Constants.STORE_POINTS, Context.MODE_PRIVATE)
        mSharedPrefAboutAppDialog =
            getSharedPreferences(Constants.CUSTOM_DIALOG, Context.MODE_PRIVATE)
        mSharedPrefFreePoints = getSharedPreferences(Constants.FREE_POINTS, Context.MODE_PRIVATE)

        if (!mSharedPrefAboutAppDialog.getBoolean(Constants.CUSTOM_DIALOG, false)) {
            showCustomDialog()
        }

        if (!mSharedPrefFreePoints.getBoolean(Constants.FREE_POINTS_BOOL, false)) {
            val editorFreePoints = mSharedPrefFreePoints.edit()
            editorFreePoints.putBoolean(Constants.FREE_POINTS_BOOL, true)
            editorFreePoints.putInt(Constants.FREE_POINTS, 50)
            editorFreePoints.apply()
            editorFreePoints.commit()
        }

        if (mSharedPrefPointsStoreMidNight.contains(Constants.NO_INTERNET_POINT_STORE)
            && isInternetConnected(this@MainActivity)
        ) {
            Log.e("Share", "Working")
            val updatedPoints =
                mSharedPrefPointsStoreMidNight.getLong(Constants.NO_INTERNET_POINT_STORE, 0L)
            val userHashMap = HashMap<String, Any>()
            userHashMap[Constants.POINTS] = updatedPoints
            val editor = mSharedPrefPointsStoreMidNight.edit()
            editor.remove(Constants.NO_INTERNET_POINT_STORE)
            editor.clear()
            editor.apply()
            editor.commit()
            FireStoreClass().updateProfileData(this, userHashMap)
        }

        binding.apply {
            shareButton.setOnClickListener {
                shareAppLinkRecommendFriend()
            }
            settingsButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            helpButton.setOnClickListener {
                openGmailHelpButton()
            }
            aboutAppButton.setOnClickListener {
                showCustomDialog()
            }
            navDraawerHeaderInclude.logOutButton.setOnClickListener {
                signOut()
            }
            navDraawerHeaderInclude.userImageNavHeader.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivityForResult(intent, MY_PROFILE_REQ_CODE)
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavBar = binding.includeAppBarLayout.MainScreenUsageActivity.bottomBar
        bottomNavBar.setupWithNavController(navController)

    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.includeAppBarLayout.toolbar)
        val title = SpannableString("NOUZZE")
        title.setSpan(
            StyleSpan(Typeface.BOLD),
            0, // Start index
            title.length, // End index (length of the title)
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        supportActionBar?.title = title
        binding.includeAppBarLayout.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.includeAppBarLayout.toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }
    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== MY_PROFILE_REQ_CODE){
            FireStoreClass().loadUserData(this)
            toggleDrawer()
        }
        else{
            Log.e("Cancelled","Cancelled")
        }
    }
    fun updateNavigationUserDetails(user: UserDetails){
        mUserDetails = user
        mSharedPrefMidNightUserDetails.saveDataObject(Constants.MID_NIGHT_USER_DATA, mUserDetails)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = user.email
        binding.navDraawerHeaderInclude.userNameNavHeader.text = user.name
        binding.includeAppBarLayout.userPoints.text = user.points.toString()

    }

    private fun formatNumber(number: Long): String {
        return when {
            number in 1000..999999 -> {
                val thousands = number / 1000
                "${thousands}k"
            }
            number >= 1000000 -> {
                val millions = number / 1000000
                "${millions}M"
            }
            else -> number.toString()
        }
    }
    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun shareAppLinkRecommendFriend() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain" // Set the type to plain text for sharing a link
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.screentimex.nouzze") // Replace with the URL you want to share
        startActivity(Intent.createChooser(shareIntent, "Share Link"))
    }
    private fun openGmailHelpButton() {
        val email = "nouzzehelp@gmail.com"
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse("mailto:$email"))
        startActivity(intent)
    }
    private fun showToast(error: String) {
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
    }
    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_main_screen,null )
        val dialogText = dialogView.findViewById<TextView>(R.id.messageTextView)
        val text = "Welcome to Nouzze. \uD83D\uDC80 \nWe're excited to have you on board. With Nouzze, you can earn coins by using other apps. Here's a quick guide to get you started:\n" +
                "\n" +
                "Use other apps like Snapchat, Instagram, zomato and many more.\n" +
                "The more time you spend on those apps, the more coins you'll earn.\n" +
                "Collect coins and redeem rewards in our store.\n" +
                "If you have any questions or need assistance, don't hesitate to contact our support team at (nouzzehelp@gmail.com). Happy earning!" +
                "\n" +  "\n" + "An app in which you can earn, without using the app itself."

        val spannableText = SpannableStringBuilder(text)

        val startIndex = text.indexOf("An app in which you can earn, without using the app itself.")
        val endIndex = startIndex + "An app in which you can earn, without using the app itself.".length

        spannableText.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialogText.text = spannableText

        builder.setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Ok Got It !!"){ _, _ ->
                val editor = mSharedPrefAboutAppDialog.edit()
                editor.putBoolean(Constants.CUSTOM_DIALOG, true)
                editor.apply()
            }.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

}