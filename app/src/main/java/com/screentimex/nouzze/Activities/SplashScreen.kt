package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.Firebase.SignInActivity
import com.screentimex.nouzze.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val currentUserId = FireStoreClass().getCurrentUUID()
            if(currentUserId.isNotEmpty()){
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this@SplashScreen, SignInActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}