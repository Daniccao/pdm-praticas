package com.weatherapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weatherapp.api.WeatherService
import com.weatherapp.repo.Repository

class MainViewModelFactory(
    private val repository: Repository,
    private val weatherService: WeatherService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, weatherService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
