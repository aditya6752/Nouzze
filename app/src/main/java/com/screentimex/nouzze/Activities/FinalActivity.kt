package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.Firebase.SignInActivity
import com.screentimex.nouzze.R

class FinalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            startActivity(Intent(this@FinalActivity, MainActivity::class.java))
            finish()
        }, 5000)
    }
}