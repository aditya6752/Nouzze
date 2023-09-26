package com.screentimex.nouzze.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.databinding.ActivityMainBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateSpinner()
    }

    private fun updateSpinner() {
        val options = listOf("Choose your profession",
            "Option 1",
            "Option 2",
            "Option 3"
        )
        val spinner = binding.professionOptions
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }
}