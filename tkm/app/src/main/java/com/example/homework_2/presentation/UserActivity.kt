package com.example.homework_2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_2.R
import com.example.homework_2.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileFragment = ProfileFragment()
        profileFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction()
            .replace(R.id.user_activity, profileFragment)
            .commit()
    }
}