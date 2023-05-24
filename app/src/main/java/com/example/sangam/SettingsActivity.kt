package com.example.sangam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sangam.databinding.ActivityFeedbackBinding
import com.example.sangam.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        binding.coolPinkTheme.setOnClickListener {

        }
    }
    
}