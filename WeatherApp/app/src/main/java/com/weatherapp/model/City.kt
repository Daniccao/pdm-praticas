package com.weatherapp.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng


data class City(
    val name: String,
    var location: LatLng? = null,
    var weather: Weather? = null,
    var forecast: List<Forecast>? = null,
    var img_url: String? = null,
    var bitmap: Bitmap? = null,
    var isMonitored: Boolean = false
)
