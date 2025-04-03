package com.example.enursery.presentation.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.enursery.R
import com.example.enursery.core.di.Injection
import com.example.enursery.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.logooo)
            setDisplayUseLogoEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        lifecycleScope.launch {
            val seeder = Injection.provideSeeder(this@AuthActivity)
            seeder.seedIfNeeded()
        }
    }
}