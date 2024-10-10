package com.weatherapp

import android.app.Application
import android.content.Intent
import com.weatherapp.db.fb.FBAuth
import com.weatherapp.monitor.ForecastMonitor
import com.weatherapp.repo.Repository
import com.weatherapp.ui.LoginPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val fbAuth = FBAuth ()
        MainScope().launch {
            fbAuth.currentUserFlow.collect { user ->
                if (user == null) goToLogin()
                else goToMain()
            }
        }
    }
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
