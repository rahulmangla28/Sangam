package com.example.sangam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sangam.databinding.ActivityAboutBinding
import com.example.sangam.databinding.ActivityFeedbackBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding : ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"

        binding.aboutText.text = aboutText()
    }

    private fun aboutText() : String {
        return "Developed By \n  ~ Rahul Mangla " +
                "\n\n I will love to hear your feedback"
    }
}