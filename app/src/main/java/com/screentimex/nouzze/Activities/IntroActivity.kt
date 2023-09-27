package com.screentimex.nouzze.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.screentimex.nouzze.Firebase.SignUpActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityMainBinding
import com.screentimex.nouzze.models.Constants

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateSpinner()
        binding.nextButton.setOnClickListener {
            val okCreds = validateForm()
            if(okCreds.isBlank()) {
                val details = arrayOf(binding.userName.text.toString(),
                    binding.userAge.text.toString(),
                    binding.professionOptions.selectedItem.toString())
                val intent = Intent(this@IntroActivity, SignUpActivity::class.java)
                intent.putExtra(Constants.INTRODETAILS, details)
                startActivity(intent)
            } else {
                showError(okCreds)
            }
        }
    }

    private fun updateSpinner() {
        val options = listOf("Choose your profession",
            "Student", "Engineer", "Bank", "Doctor", "Teacher", "Business", "Media",
            "Sales", "Social Work", "Sports", "Driver", "House Maker", "Other"
        )
        val spinner = binding.professionOptions
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }

    private fun validateForm(): String{
        var lis = ""
        if(binding.userName.text.toString().isEmpty())
            lis = "Name"
        else if(binding.userAge.text.toString().isEmpty())
            lis = "Age"
        else if(binding.professionOptions.selectedItem.toString() == "Choose your profession")
            lis = "Profession"
        return lis
    }

    fun showError(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@IntroActivity,
                R.color.snackbarcolor
            )
        )
        snackBar.show()
    }
}