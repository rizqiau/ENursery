package com.example.enursery.presentation.startup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.presentation.MainActivity
import com.example.enursery.presentation.auth.AuthActivity

class StartupActivity : AppCompatActivity() {
    private lateinit var viewModel: StartupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[StartupViewModel::class.java]

        if (viewModel.isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}
